package com.txy.sql.domain;

import java.util.*;

/**
 * @Auther: tianxiayu
 * @Date: 2020/9/15 14:21
 */
public class Table extends LinkedHashMap<String, List<String>> {
    public  Table(String[] colNames){
        for(String colName : colNames){
            this.put(colName, new ArrayList<String>());
        }
    }
}