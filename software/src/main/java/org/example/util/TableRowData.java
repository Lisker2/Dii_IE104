package org.example.util;

public class TableRowData {

    public int instance;

    public String name;
    public String algorithm;
    public String budget;

    public String time;
    public TableRowData (int instance,String name, String algorithm, String budget, String time){
        this.instance = instance;
        this.name = name;
        this.algorithm = algorithm;
        this.budget = budget;
        this.time = time;
    }
}