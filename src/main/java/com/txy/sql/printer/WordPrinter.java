package com.txy.sql.printer;


import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.txy.sql.domain.Table;
import com.txy.sql.domain.Tables;
import com.txy.sql.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @Auther: tianxiayu
 * @Date: 2020/9/15 19:24
 */
public class WordPrinter implements Printer {

    public void print(Tables tables, String databaseName){
        try {
            // 1.新建document对象
             Document document = new Document(PageSize.A4);

            File dir = new File("file");
            if(!dir.exists()){
                dir.mkdirs();
            }
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File("file/" + databaseName + "-" + Calendar.getInstance().getTimeInMillis() + ".doc");
            file.createNewFile();

            RtfWriter2.getInstance(document, new FileOutputStream(file));

            // 3.打开文档
            document.open();

            // 4.向文档中添加内容
            generateWORD(document, tables);

            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 填充数据
    private void generateWORD(Document document, Tables tables) throws Exception {
        for(Map.Entry<String, Table> entry1 : tables.entrySet()){
            String tableName = entry1.getKey();
            Table table = entry1.getValue();

            // 表名
            document.add(new Paragraph("表名：" + tableName));

            // 表格
            //PdfPTable pdfPTable = createTable(new float[] {40, 120, 120, 120, 80, 80 });
            com.lowagie.text.Table wordTable = new com.lowagie.text.Table(table.size());
            int rowCount = 0;
            for(Map.Entry<String, java.util.List<String>> column : table.entrySet()){
                String columnName = column.getKey();
                rowCount = column.getValue().size();
                wordTable.addCell(StringUtil.getUTF8StringFromGBKString(columnName));
            }

            for(int i = 0; i < rowCount; i++){
                for(Map.Entry<String, java.util.List<String>> column : table.entrySet()){
                    List<String> elements = column.getValue();
                    wordTable.addCell(StringUtil.getUTF8StringFromGBKString(elements.get(i)));
                }
            }

            document.add(wordTable);
            document.add(new Paragraph());
        }
    }
}