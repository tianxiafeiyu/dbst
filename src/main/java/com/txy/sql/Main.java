package com.txy.sql;

import com.txy.sql.scanner.CMDScanner;
import com.txy.sql.domain.Tables;
import com.txy.sql.exporter.Exporter;
import com.txy.sql.printer.PdfPrinter;
import com.txy.sql.printer.Printer;
import com.txy.sql.printer.WordPrinter;

import java.util.List;

/**
 * @Auther: tianxiayu
 * @Date: 2020/9/15 12:09
 */
public class Main {
    public static void main(String[] args) {
        CMDScanner cmdScanner = new CMDScanner();
        cmdScanner.start();
        // 获取连接信息
        cmdScanner.getConnectionInfo();

        Exporter exporter = new Exporter(CMDScanner.DB_ADDRESS, CMDScanner.DB_USER, CMDScanner.DB_PASSWORD);

        List<String> databaseNameList = exporter.getDatabaseNameLsit();
        // 获取要采集的数据库
        cmdScanner.getDBName(databaseNameList);

        Tables tables = exporter.collect(CMDScanner.DB_NAME);

        Printer pdfPrinter = new PdfPrinter();
        pdfPrinter.print(tables, CMDScanner.DB_NAME);

        Printer wordPrinter = new WordPrinter();
        wordPrinter.print(tables, CMDScanner.DB_NAME);

        cmdScanner.end();

    }
}