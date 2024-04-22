package org.example.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class createNearestCities {
    String directory;
    String instance;

    public createNearestCities(String directory, String instance) {
        this.directory = directory;
        this.instance = instance;
    }

    public createNearestCities() {
    }

    public void create() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.directory + this.instance + ".tsp"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.directory + this.instance + "NearestCities.txt"));

            List<Point> location = new ArrayList<>();
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if(count < 6){
                    count ++;
                    continue;
                }
                String[] parts = line.trim().split("\\s+");
                if(parts.length >1){
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    location.add(new Point(x, y));
                }
            }
            reader.close();

            List<List<Integer>> result = new ArrayList<>();
            for (Point point : location) {
                Map<Integer, Double> nearest8 = new HashMap<>();
                for (int i = 0; i < location.size(); i++) {
                    if (i == location.indexOf(point)) continue;
                    double distance = Math.sqrt(Math.pow(point.x - location.get(i).x, 2) + Math.pow(point.y - location.get(i).y, 2));
                    if (nearest8.size() == 8) {
                        int lkey = -1;
                        double lvalue = Double.MIN_VALUE;
                        for (Map.Entry<Integer, Double> entry : nearest8.entrySet()) {
                            if (entry.getValue() >= lvalue) {
                                lkey = entry.getKey();
                                lvalue = entry.getValue();
                            }
                        }
                        if (lvalue >= distance) {
                            nearest8.remove(lkey);
                            nearest8.put(i, distance);
                        }
                    } else {
                        nearest8.put(i, distance);
                    }
                }
                List<Integer> cities = new ArrayList<>(nearest8.keySet());
                result.add(cities);
            }

            for (List<Integer> cities : result) {
                StringBuilder lineBuilder = new StringBuilder();
                for (int city : cities) {
                    lineBuilder.append(city).append(" ");
                }
                writer.write(lineBuilder.toString().trim());
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Point {
        double x, y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
