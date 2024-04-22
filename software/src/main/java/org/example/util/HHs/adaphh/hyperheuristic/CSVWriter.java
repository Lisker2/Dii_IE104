package org.example.util.HHs.adaphh.hyperheuristic;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {

    public static void main(String[] args) {
        int[][] myArray = {
            {1, 2},
            {3, 4},
            {5, 7}
        };

        String fileName = "results/output.csv";

        try {
            FileWriter writer = new FileWriter(fileName);

            // Write the column headers (first row)
            writer.append("row_index,run1,run2\n");

            // Write the data rows
            for (int i = 0; i < myArray.length; i++) {
                writer.append(i + "," + myArray[i][0] + "," + myArray[i][1] + "\n");
            }

            writer.flush();
            writer.close();
            System.out.println("CSV file has been created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}
