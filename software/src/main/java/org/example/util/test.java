package org.example.util;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        String test = "solution_test_algo_5.txt";
        String[] all_fields = test.substring(0, test.lastIndexOf(".txt")).split("_");
        System.out.println(Arrays.toString(all_fields));
    }
}
