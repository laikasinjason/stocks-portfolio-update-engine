package com.laikasin.editexcel;

import com.laikasin.datamodel.Account;
import com.laikasin.datamodel.PortfolioPnL;
import com.laikasin.datamodel.StockPerformance;
import com.laikasin.indicator.EMA;
import com.laikasin.indicator.RSI;
import com.laikasin.parser.StockWebPageYahooAPIParser;
import com.laikasin.utils.StockCodeStringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUpdater {
    private static Logger log = LoggerFactory.getLogger(ExcelUpdater.class);


    private String fileName;

    private Map<String, StockWebPageYahooAPIParser> parserMap;

    public ExcelUpdater(String fileName) {
        this.fileName = fileName;
        parserMap = new HashMap<>();
    }

    public void update() {
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", e);
            return;
        }

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(excelFile);

            Sheet sheet = workbook.getSheetAt(1);

            int rows = sheet.getPhysicalNumberOfRows(); // No of rows

            Row row;

            Cell lastDayPrice = sheet.getRow(2).getCell(5);
            Cell currentPrice = sheet.getRow(3).getCell(5);
            lastDayPrice.setCellValue(currentPrice.getNumericCellValue());

            // Update each stock's price
            for (int r = 6; r <= rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    Cell stockCodeCell = row.getCell(0);

                    if (stockCodeCell != null) {
                        String ricCode = StockCodeStringUtil.resolveToRicCode((int) stockCodeCell.getNumericCellValue());

                        StockWebPageYahooAPIParser parser = new StockWebPageYahooAPIParser(ricCode);
                        BigDecimal price = parser.parse();
                        parserMap.put(ricCode, parser);

                        Cell currentPriceCell = row.getCell(5);
                        if (price != null) {
                            currentPriceCell.setCellValue(price.doubleValue());
                            log.info("Set price {} to ric {}.", price, ricCode);
                        } else {
                            log.error("Ric {} price is null.", ricCode);
                        }
                    }

                }
            }
            CreationHelper createHelper = workbook.getCreationHelper();

            Cell modifiedTimeCell = sheet.getRow(0).createCell(1);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm:ss"));
            modifiedTimeCell.setCellValue(new Date());
            modifiedTimeCell.setCellStyle(cellStyle);

            createHelper.createFormulaEvaluator().evaluateAll();
            logPortfolioValue(workbook);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PortfolioPnL read() {
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", e);
            return null;
        }

        Workbook workbook;
        PortfolioPnL portfolioPnL = new PortfolioPnL();
        List<StockPerformance> stockPerformanceList = new ArrayList<>();
        try {
            workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(1);
            Iterator<Row> iterator = sheet.iterator();

            // Set portfolio statistics
            portfolioPnL.setModifiedTime(String.valueOf(sheet.getRow(0).getCell(1).getDateCellValue()));
            portfolioPnL.setPortfolioBoughtValue(sheet.getRow(3).getCell(3).getNumericCellValue());
            portfolioPnL.setPortfolioLastDayValue(sheet.getRow(2).getCell(5).getNumericCellValue());
            portfolioPnL.setPortfolioLastDayPercent(sheet.getRow(2).getCell(7).getNumericCellValue() * 100);
            portfolioPnL.setCash(sheet.getRow(2).getCell(10).getNumericCellValue());
            portfolioPnL.setPortfolioCurrentValue(sheet.getRow(3).getCell(5).getNumericCellValue());
            portfolioPnL.setDiff(sheet.getRow(3).getCell(6).getNumericCellValue());
            portfolioPnL.setPortfolioCurrentPercent(sheet.getRow(3).getCell(7).getNumericCellValue() * 100);

            // Go to the first line of the table
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getRowNum() == 5) {
                    break;
                }
            }

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getCell(0) == null) {
                    break;
                }
                log.info("Parsing row: {}", currentRow.getRowNum());
                if (currentRow.getCell(10) != null) {
                    // only fetch the final entry (summed entry)
                    StockPerformance stockPerformance = new StockPerformance();
                    String stockCode = StockCodeStringUtil.resolveToRicCode((int) currentRow.getCell(0).getNumericCellValue());
                    stockPerformance.setStockCode(stockCode);
                    stockPerformance.setAccount(Account.fromShortChar(currentRow.getCell(1).getStringCellValue().toCharArray()[0]));
                    stockPerformance.setAmount((int) currentRow.getCell(2).getNumericCellValue());
                    stockPerformance.setBoughtPrice(currentRow.getCell(3).getNumericCellValue());
                    stockPerformance.setBoughtValue(currentRow.getCell(4).getNumericCellValue());
                    stockPerformance.setCurrentPrice(currentRow.getCell(5).getNumericCellValue());
                    stockPerformance.setCurrentValue(currentRow.getCell(6).getNumericCellValue());
                    stockPerformance.setDiff(currentRow.getCell(7).getNumericCellValue());
                    stockPerformance.setRatio(currentRow.getCell(8).getNumericCellValue());
                    stockPerformance.setStockName(currentRow.getCell(9).getStringCellValue());

                    RSI rsiIndicator = new RSI(parserMap.get(stockCode).getQuoteHistory(), 14, stockCode);
                    Double rsiValue = rsiIndicator.calculate(0);
                    stockPerformance.setRsi(rsiValue);

                    EMA emaIndicator = new EMA(parserMap.get(stockCode).getQuoteHistory(), 12);
                    Double emaValue = emaIndicator.calculate(0);
                    stockPerformance.setEma10(emaValue);

                    // getting the T-1 price
                    stockPerformance.setLastDayPrice(parserMap.get(stockCode).getQuoteHistory().getLastPriceBar(1).getClose().doubleValue());
                    stockPerformance.setLastDayEma10(emaIndicator.calculate(1));
                    log.info("Stocks [{}] : RSI = {} , EMA = {}", stockCode, rsiValue, emaValue);

                    stockPerformanceList.add(stockPerformance);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        portfolioPnL.setStockPerformanceList(stockPerformanceList);

        log.info("Portfolio pnL: {}", portfolioPnL.toString());
        return portfolioPnL;
    }


    private void logPortfolioValue(Workbook workbook) {
        Sheet returnSheet = workbook.getSheetAt(2);
        Sheet portfolioSheet = workbook.getSheetAt(1);

        Row editRow = returnSheet.createRow(returnSheet.getLastRowNum() + 1);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = format.format(new Date());

        Cell newDateCell = editRow.createCell(0);
        Cell newValueCell = editRow.createCell(1);
        Cell newCashCell = editRow.createCell(2);
        Cell newTotalCell = editRow.createCell(3);

        newDateCell.setCellValue(new Date());
        newValueCell.setCellValue(portfolioSheet.getRow(3).getCell(5).getNumericCellValue());
        newCashCell.setCellValue(portfolioSheet.getRow(2).getCell(10).getNumericCellValue());

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("mm/dd/yyyy"));
        newDateCell.setCellStyle(cellStyle);
        String formula = "B".concat(String.valueOf(editRow.getRowNum() + 1)).concat(":C").concat(String.valueOf(editRow.getRowNum() + 1));
        newTotalCell.setCellFormula("SUM(" + formula + ")");
        workbook.getCreationHelper().createFormulaEvaluator().evaluate(newTotalCell);
    }
}
