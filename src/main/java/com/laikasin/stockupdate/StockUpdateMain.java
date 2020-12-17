package com.laikasin.stockupdate;

import com.laikasin.datamodel.PortfolioPnL;
import com.laikasin.editexcel.ExcelUpdater;
import com.laikasin.emailgenerator.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockUpdateMain {
    private static Logger log = LoggerFactory.getLogger(StockUpdateMain.class);
    private static String EMAIL = "email";

    public static void main(String[] args) {

        final String FILE_NAME = args[0];
        boolean emailSwitch = false;
        if (args.length > 1 && EMAIL.equals(args[1])) {
            emailSwitch = true;
        }


        ExcelUpdater excelUpdater = new ExcelUpdater(FILE_NAME);
        excelUpdater.update();

        log.info("Finish updating stocks booking excel!!");

        if (emailSwitch) {
            PortfolioPnL portfolioPnL = excelUpdater.read();

            EmailSender emailSender = new EmailSender();
            emailSender.send(portfolioPnL);
        }

        log.info("Finish updating pnl report!!");

        System.exit(0);
    }
}