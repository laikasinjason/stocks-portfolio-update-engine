package com.laikasin.emailgenerator;

import com.laikasin.datamodel.PortfolioPnL;
import com.laikasin.datamodel.StockPerformance;
import com.laikasin.utils.NumberUtils;
import com.laikasin.utils.PortfolioPnLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class EmailSender {
    protected static final Logger log = LoggerFactory.getLogger(EmailSender.class);
    private Session session;
    private Properties properties;

    public EmailSender() {
        InputStream input = null;
        properties = new Properties();
        try {

            input = EmailSender.class.getClassLoader().getResourceAsStream("config.properties");

            // load a properties file
            properties.load(input);

            // get the property value and print it out
            log.info("Email user: {}", properties.getProperty("email.user"));
            log.info("Email password: {}", properties.getProperty("email.password"));

        } catch (IOException ex) {
            log.error("Error reading properties {}", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("Error closing io {}", e);
                }
            }
        }

        session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("email.user"), properties.getProperty("email.password"));
                    }
                });
    }


    public void send(PortfolioPnL portfolioPnL) {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("email.user")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(properties.getProperty("email.to")));
            message.setSubject("Stocks pnl report");
            message.setText("Dear Mail Crawler," +
                    "\n\n No spam to my email, please!");
            String html = createHtmlContent(portfolioPnL);
            message.setDataHandler(new DataHandler(new HTMLDataSource(html)));

            Transport.send(message);

            log.info("Done sending email!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createHtmlContent(PortfolioPnL portfolioPnL) {
        String html = "<html><head>"
                + "<title>PnL</title>"
                + "</head>" + "<LINK REL='stylesheet' HREF='stylesheet/fac_css.css' TYPE='text/css'>"
                + "<body>"
                + "<table border='1' width='1000' cellpadding='2' cellspacing='1' bgColor='#CD919E' style='border-collapse: collapse' bordercolor='#EBDA2A' align='left'>"
                + "<tr><td  class ='text12' width='100%'>" + "Modified Time: " + portfolioPnL.getModifiedTime() + "</td></tr>"
                + "<tr><td  class ='text12' width='100%'>" + "Total Bought Value: " + portfolioPnL.getPortfolioBoughtValue() + "</td></tr>"
                + "<tr><td  class ='text12' width='100%'>" + "Total Last day Value: " + portfolioPnL.getPortfolioLastDayValue() +
                "  " + String.format("%.2f%%", portfolioPnL.getPortfolioLastDayPercent())
                + "  Account: " + PortfolioPnLUtils.getAccountBoughtValueMap(portfolioPnL).toString() + "</td></tr>"
                + "<tr><td  class ='text12' width='100%'>" + "Total Current Value: " + portfolioPnL.getPortfolioCurrentValue() +
                "  " + String.format("%.2f%%", portfolioPnL.getPortfolioCurrentPercent())
                + "  Account: " + PortfolioPnLUtils.getAccountBoughtValueMap(portfolioPnL).toString() + "</td></tr>"
                + "<tr><td  class ='text12' width='100%'>" + "Diff: " + portfolioPnL.getDiff() + " ( " + NumberUtils.formatDouble(portfolioPnL.getPecentChange()) + "% , " +
                +(portfolioPnL.getPortfolioCurrentValue() - portfolioPnL.getPortfolioLastDayValue()) + " )</td></tr>"
                + "<tr><td  class ='text12' width='100%'>" + "Cash capital: " + NumberUtils.formatDouble(portfolioPnL.getCash()) + "</td></tr>"
                + "</table>"
                + "<table border='1' width='1000' cellpadding='2' cellspacing='1' bgColor='#B6AFA9' style='border-collapse: collapse' bordercolor='#EBDA2A' align='left'>"
                + "<tr bgColor=#CD919E class='centerheading' align='left'>"
                + "<td width='30' style='color: #FFFFFF;'><b>Stocks</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>StockName</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Account</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Amount</b></td>"
                + "<td width='35' style='color: #FFFFFF;'><b>Bought price</b></td>"
                + "<td width='35' style='color: #FFFFFF;'><b>Bought value</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Current price</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Current value</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Diff</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Rsi</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Ratio</b></td>"
                + "<td width='30' style='color: #FFFFFF;'><b>Remarks</b></td>"
                + "</tr>";
        String tableContent = "";

        final StringBuilder stringBuffer = new StringBuilder(5000);
        for (StockPerformance stockPerformance : portfolioPnL.getStockPerformanceList()) {

            stringBuffer.append("<tr>").append("<td width='30' style='color: #EEE9E9;'>").append(stockPerformance.getStockCode()).append("</b></td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(stockPerformance.getStockName()).append("</b></td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(stockPerformance.getAccount().toShortChar()).append("</b></td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(stockPerformance.getAmount()).append("</b></td>")
                    .append("<td width='35' style='color: #EEE9E9;'>").append(NumberUtils.formatDouble(stockPerformance.getBoughtPrice())).append("</td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(NumberUtils.formatDouble(stockPerformance.getBoughtValue())).append("</td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(NumberUtils.formatDouble(stockPerformance.getCurrentPrice()))
                    .append(" (").append(NumberUtils.formatDouble(getPricePercentage(stockPerformance))).append("%)").append("</td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(NumberUtils.formatDouble(stockPerformance.getCurrentValue())).append("</td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(NumberUtils.formatDouble(stockPerformance.getDiff())).append("</td>")
                    .append("<td width='30' style='color: #EEE9E9;'>").append(NumberUtils.formatDouble(stockPerformance.getRsi())).append("</td>")
                    .append("<td width='30' style='color: ").append((stockPerformance.getRatio() < 1 ? "#FC0000" : "#EEE9E9") + ";'>" + NumberUtils.formatDouble(stockPerformance.getRatio()))
                    .append("<td width='30' style='color: #EEE9E9;'>").append(constructRemark(stockPerformance)).append("</td>")
                    .append("</td></tr>");

        }
        html = html.concat(stringBuffer.toString()).concat("</table>" + "</body></html>");
        return html;
    }

    private String constructRemark(StockPerformance stockPerformance) {
        String remark = null;
        if (stockPerformance.getEma10() < stockPerformance.getCurrentPrice() && stockPerformance.getLastDayEma10() > stockPerformance.getLastDayPrice()) {
            remark = "Break above EMA 10";
        } else if (stockPerformance.getEma10() > stockPerformance.getCurrentPrice() && stockPerformance.getLastDayEma10() < stockPerformance.getLastDayPrice()) {
            remark = "Break below EMA 10";
        } else {
            remark = "--";
        }
        return remark;
    }

    private Double getPricePercentage(StockPerformance stockPerformance) {
        return (stockPerformance.getCurrentPrice() - stockPerformance.getLastDayPrice()) / stockPerformance.getLastDayPrice() * 100;
    }
}
