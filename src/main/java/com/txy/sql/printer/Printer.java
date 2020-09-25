package com.txy.sql.printer;

import com.txy.sql.domain.Tables;

/**
 * @Auther: tianxiayu
 * @Date: 2020/9/25 10:56
 */
public interface Printer {
    void print(Tables tables, String databaseName);
}
