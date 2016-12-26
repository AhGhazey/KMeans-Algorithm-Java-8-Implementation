/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import sun.util.calendar.LocalGregorianCalendar.Date;

/**
 *
 * @author aghazey
 */
public class KMeans {

    private List<House> houses;
    private List<Cluster> clusters;
    private List<Column> data_columns;
    private int numberOfClusters;
    private int distinctValus;
    private int dataSize = 0;

    public KMeans(int numberOfClusters, int distinctValus) {
        this.numberOfClusters = numberOfClusters;
        this.distinctValus = distinctValus;
        houses = new ArrayList<>();
        clusters = new ArrayList<>(numberOfClusters);
        data_columns = new ArrayList<>();
    }

    private void readData() {
        File file = new File("HouseDataML.csv");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            String line = scanner.nextLine();
            String[] headers = line.split(",");
            for (String header : headers) {
                data_columns.add(new Column(header));
            }
            while (scanner.hasNextLine()) {
                dataSize++;
                line = scanner.nextLine();
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    double value = Double.parseDouble(values[i]);
                    data_columns.get(i).addValue(value);
                }
            }

        } catch (FileNotFoundException ex) {
            System.out.println(file.getAbsolutePath());
            Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public void Init() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        //read data
        readData();
        //filter columns to columns which distinct values > d
        List<Column> columns = data_columns.stream().filter(col -> col.getDistinctValuesCount() > distinctValus).collect(toList());
        for (int i = 0; i < dataSize; i++) {
            House house = new House();
            for (Column column : columns) {
                //reflection get class attribute by its name as String 
                Field filed = house.getClass().getDeclaredField(column.getLabel());
                filed.setDouble(house, column.getValue(i));
            }
            houses.add(house);
        }
        //adding clusters + random centroid between min and max value for each field
//        for (int i = 0; i < numberOfClusters; i++) {
//            House centroid = new House();
//            for (Column column : columns) {
//                Random r = new Random();
//                //reflection
//                Field filed = centroid.getClass().getDeclaredField(column.getLabel());
//                filed.setDouble(centroid, (column.minValue + (column.maxValue - column.minValue) * r.nextDouble()));
//            }
//            Cluster cluster = new Cluster(i);
//            cluster.setCentroid(centroid);
//            clusters.add(cluster);
//        }
        Random r = new Random();
        for (int i = 0; i < numberOfClusters; i++) {
            int index = (int) ((int) dataSize * r.nextDouble());
            House centroid = houses.get(index).copy();
            Cluster cluster = new Cluster(i);
            cluster.setCentroid(centroid);
            clusters.add(cluster);
        }

        data_columns = columns;

        //plot clusters initial
        printClusters();
    }

    //The process to calculate the K Means, with iterating method.
    public void calculate() {
        boolean finish = false;
        int iteration = 0;

        // Add in new data, one at a time, recalculating centroids with each new one. 
        while (!finish) {
            //Clear cluster state
            clearClusters();

            List<House> lastCentroids = getCentroids();

            //Assign houses to the closer cluster
            assignCluster();

            //Calculate new centroids.
            calculateCentroids();

            iteration++;

            List<House> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            double distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += getDistance(lastCentroids.get(i), currentCentroids.get(i));
            }
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);
            //printClusters();
            //check convergence
            if (distance == 0) {
                finish = true;
                try {
                    //FileOutputStream fo = new FileOutputStream();
                    Writer wr = new FileWriter(new File("output.txt"));
                    clusters.stream().forEach((cluster) -> {
                        try {
                            wr.write(cluster.toString() + "\n");
                        } catch (IOException ex) {
                            Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                    wr.close();
                } catch (java.io.IOException ex) {
                    Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void printClusters() {
        clusters.stream().forEach((cluster) -> {
            cluster.printCluster();
        });
    }

    private void clearClusters() {
        clusters.stream().forEach((cluster) -> {
            cluster.clear();
        });
    }

   

    private List<House> getCentroids() {
        List<House> centroids = new ArrayList(numberOfClusters);
        // copy centroids into new list
        clusters.stream().map((cluster) -> cluster.getCentroid()).map((aux) -> aux.copy()).forEach((newHouse) -> {
            centroids.add(newHouse);
        });
        return centroids;
    }

    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min;
        int clusterIndex = -1;
        double distance;

        for (House house : houses) {
            min = max;
            for (int i = 0; i < numberOfClusters; i++) {
                Cluster c = clusters.get(i);
                distance = house.getDistance(c.getCentroid());
                if (distance < min) {
                    min = distance;
                    clusterIndex = i;
                }
            }
            house.setCluster(clusters.get(clusterIndex));
            clusters.get(clusterIndex).addHouse(house);
        }
    }

    private void calculateCentroids() {
        clusters.stream().forEach((cluster) -> {
            List<House> list = cluster.getHouses();

            int n_houses = list.size();
            if (n_houses > 0) {
                double priceAvarage = list.stream().mapToDouble(house -> house.getPrice()).average().getAsDouble();
                double bedRoomsAvarage = list.stream().mapToDouble(house -> house.getBedRooms()).average().getAsDouble();
                double pathRoomsAvarage = list.stream().mapToDouble(house -> house.getPathRooms()).average().getAsDouble();
                double sqftLivingAvarage = list.stream().mapToDouble(house -> house.getSqftLiving()).average().getAsDouble();
                double sqftLotAvarage = list.stream().mapToDouble(house -> house.getSqftLot()).average().getAsDouble();
                double latitudeAvarage = list.stream().mapToDouble(house -> house.getLatitude()).average().getAsDouble();
                double longitudeAvarage = list.stream().mapToDouble(house -> house.getLongitude()).average().getAsDouble();
                House centroid = new House();         
                centroid.price = priceAvarage;
                centroid.bedRooms = bedRoomsAvarage;
                centroid.pathRooms = pathRoomsAvarage;
                centroid.sqftLiving = sqftLivingAvarage;
                centroid.sqftLot = sqftLotAvarage;
                centroid.latitude = latitudeAvarage;
                centroid.longitude = longitudeAvarage;
                cluster.setCentroid(centroid);
            }
        });
    }

    //manhaten distance
    private double getDistance(House centroid1, House centroid2) {
        double distance = Math.abs(centroid1.price - centroid2.price)
                + Math.abs(centroid1.bedRooms - centroid2.bedRooms)
                + Math.abs(centroid1.pathRooms - centroid2.pathRooms)
                + Math.abs(centroid1.sqftLiving - centroid2.sqftLiving)
                + Math.abs(centroid1.sqftLot - centroid2.sqftLot)
                + Math.abs(centroid1.latitude - centroid2.latitude)
                + Math.abs(centroid1.longitude - centroid2.longitude);
        return distance;
    }
 public static void main(String[] args) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        long start = System.currentTimeMillis();
        KMeans kMeans = new KMeans(5, 10);
        kMeans.Init();
        kMeans.calculate();
        long end = System.currentTimeMillis();
        System.out.println("Time :" + (end - start));
    }
}
