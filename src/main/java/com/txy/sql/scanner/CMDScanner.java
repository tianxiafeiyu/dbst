package com.txy.sql.scanner;/**
 * @Auther: tianxiayu
 * @Date: 2020/9/25 14:10
 */

import java.util.List;
import java.util.Scanner;

/**
 * @Auther: tianxiayu
 * @Date: 2020/9/25 14:10
 */
public class CMDScanner {
    public static String DB_ADDRESS = "";
    public static String DB_USER = "";
    public static String DB_PASSWORD = "";
    public static String DB_NAME = "";

    Scanner in = new Scanner(System.in);
    public void getConnectionInfo(){
        System.out.println("请输入数据库地址(ip + 端口)：");
        DB_ADDRESS = "jdbc:mysql://" + in.nextLine();

        System.out.println("请输入用户名：");
        DB_USER = in.nextLine();

        System.out.println("请输入密码：");
        DB_PASSWORD = in.nextLine();
    }

    public void getDBName(List<String> databases){
        System.out.println("数据库列表：");
        for(int i = 0; i < databases.size(); i++){
            System.out.println(i + 1 + ". " + databases.get(i));
        }
        System.out.println("（输入序号选择要打印的数据数据）");

        int k = in.nextInt();

        DB_NAME = databases.get(k - 1);
    }

    public void start(){
        System.out.println("****************************************");
        System.out.println("******database structure exporter*******");
        System.out.println("****************************************");
    }

    public void end(){
        System.out.println("****************************************");
        System.out.println("****************complete!***************");
        System.out.println("****************************************");
    }
}