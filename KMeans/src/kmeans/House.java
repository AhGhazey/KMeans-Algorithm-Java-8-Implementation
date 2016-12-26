/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

/**
 *
 * @author aghazey
 */
public class House {

    public double price;
    public double bedRooms;
    public double pathRooms;
    public double sqftLiving;
    public double sqftLot;
    public double latitude;
    public double longitude;
    public Cluster cluster;

    public House() {
        cluster = null;
    }

    public House(String[] house) {
        this.price = Double.parseDouble(house[0]);
        this.bedRooms = Double.parseDouble(house[1]);
        this.pathRooms = Double.parseDouble(house[2]);
        this.sqftLiving = Double.parseDouble(house[3]);
        this.sqftLot = Double.parseDouble(house[4]);
        this.latitude = Double.parseDouble(house[5]);
        this.longitude = Double.parseDouble(house[6]);
        cluster = null;
    }

    public House(double price, double bedRooms, double pathRooms, double sqftLiving, double sqftLot, double latitude, double longitude) {
        this.price = price;
        this.bedRooms = bedRooms;
        this.pathRooms = pathRooms;
        this.sqftLiving = sqftLiving;
        this.sqftLot = sqftLot;
        this.latitude = latitude;
        this.longitude = longitude;
        cluster = null;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBedRooms() {
        return bedRooms;
    }

    public void setBedRooms(double bedRooms) {
        this.bedRooms = bedRooms;
    }

    public double getPathRooms() {
        return pathRooms;
    }

    public void setPathRooms(double pathRooms) {
        this.pathRooms = pathRooms;
    }

    public double getSqftLiving() {
        return sqftLiving;
    }

    public void setSqftLiving(double sqftLiving) {
        this.sqftLiving = sqftLiving;
    }

    public double getSqftLot() {
        return sqftLot;
    }

    public void setSqftLot(double sqftLot) {
        this.sqftLot = sqftLot;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
//manhaten distance
    public double getDistance(House centroid ) {
      //  House centroid = cluster.getCentroid();
        double distance = Math.abs(centroid.price - price)
                + Math.abs(centroid.bedRooms - bedRooms)
                + Math.abs(centroid.pathRooms - pathRooms)
                + Math.abs(centroid.sqftLiving - sqftLiving)
                + Math.abs(centroid.sqftLot - sqftLot)
                + Math.abs(centroid.latitude - latitude)
                + Math.abs(centroid.longitude - longitude);
        return distance;
    }
    public House copy(){
        return new House(price, bedRooms, pathRooms, sqftLiving, sqftLot, latitude, longitude);
    }
    @Override
    public String toString() {
        return "House{" + "price=" + price + ", bedRooms=" + bedRooms + ", pathRooms=" + pathRooms + ", sqftLiving=" + sqftLiving + ", sqftLot=" + sqftLot + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }

}
