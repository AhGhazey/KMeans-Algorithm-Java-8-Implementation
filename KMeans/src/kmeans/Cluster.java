/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aghazey
 */
public class Cluster{

    public int id;
    public List<House> houses;
    public House centroid;

    public Cluster(int id) {
        this.id = id;
        this.houses = new ArrayList<>();
        this.centroid = null;
    }

    public Cluster(int id, List<House> houses, House centroid) {
        this.id = id;
        this.houses = houses;
        this.centroid = centroid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<House> getHouses() {
        return houses;
    }

    public void setHouses(List<House> houses) {
        this.houses = houses;
    }

    public House getCentroid() {
        return centroid;
    }

    public void setCentroid(House centroid) {
        this.centroid = centroid;
    }

    public void addHouse(House house) {
        houses.add(house);
    }

    public void clear() {
        houses.clear();
    }

    @Override
    public String toString() {
        String string="";
        string = houses.stream().map(( house) -> "\n"+house.toString()).reduce(string, String::concat);  
        return "Cluster{" + "id=" + id + ", houses= [\n" + string + "],\n centroid=" + centroid + '}';
    }

    public void printCluster() {
        System.out.println("[Cluster: " + id + "]");
        System.out.println("[Centroid: " + centroid + "]");
        System.out.println("[# Houses:" + houses.size() +" ]");
        System.out.println("[");
        houses.stream().forEach((house) -> {
            System.out.println(house);
        });
        System.out.println("]");
    }

  
    

}
