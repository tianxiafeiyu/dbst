package com.txy.sql.exporter;

import com.txy.sql.domain.Table;
import com.txy.sql.domain.Tables;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Auther: tianxiayu
 * @Date: 2020/9/15 11:54
 */
public class Exporter {
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url;
    private static String username;
    private static String password;

    private Connection conn = null;
    private Statement stmt = null;

   /* static {
        try {
            // 1.通过当前类获取类加载器
            ClassLoader classLoader = Exporter.class.getClassLoader();
            // 2.通过类加载器的方法获得一个输入流
            InputStream in = classLoader.getResourceAsStream("config.properties");
            // 3.创建一个properties对象
            Properties props = new Properties();
            // 4.加载输入流
            props.load(in);
            // 5.获取相关参数的值
            driver = props.getProperty("driverClassName");
            url = props.getProperty("url");
            username = props.getProperty("username");
            password = props.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

   public Exporter(String url, String user, String password){
       this.url = url;
       this.username = user;
       this.password = password;

       this.init();
   }

    private void init(){

        try{
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void close(){
        if (stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 后关闭Connection
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public List<String> getDatabaseNameLsit(){
        // 获取数据库
        List<String> databaseNames = new ArrayList<String>();
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            ResultSet rs = dbMetaData.getCatalogs();
            while (rs.next()) {
                databaseNames.add(rs.getString("TABLE_CAT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databaseNames;
    }

    private List<String> getTableNameList(String databaseName){
        // 获取数据库所有表
        List<String> tableNames = new ArrayList<String>();
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            ResultSet rs = dbMetaData.getTables(databaseName, databaseName, "%",new String[] { "TABLE" });
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    public Tables collect(String databaseName){

        List<String> tableNames = this.getTableNameList(databaseName);
        Tables tables = new Tables();
        for (String tableName : tableNames) {
            Table table = new Table(new String[]{"序号", "列名", "数据类型", "长度", "主键", "允许空", "备注"});
            String sql = "select * from " + databaseName + "." + tableName;
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();

                int columnCount = meta.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    table.get("序号").add(String.valueOf(i));
                    table.get("列名").add(meta.getColumnName(i));
                    table.get("数据类型").add(meta.getColumnTypeName(i));
                    table.get("长度").add(String.valueOf(meta.getColumnDisplaySize(i)));
                    table.get("主键").add("");
                    table.get("允许空").add(meta.isNullable(i) == 1 ? "是" : "否");
                    table.get("备注").add(meta.getColumnLabel(i));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tables.put(tableName, table);
        }

        return tables;
    }
}