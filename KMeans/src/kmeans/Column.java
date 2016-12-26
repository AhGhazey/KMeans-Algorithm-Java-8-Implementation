/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.util.*;

/**
 *
 * @author aghazey
 */
public class Column {

    double maxValue = Double.MIN_VALUE;
    double minValue = Double.MAX_VALUE;
    Set<Double> distinctValues;
    List<Double> values;
    String label;

    public Column(String label) {
        this.label = label;
        distinctValues = new HashSet<>();
        values = new ArrayList<>();
    }

    public int getDistinctValuesCount() {
        return distinctValues.size();
    }

    public void addValue(double value) {
        values.add(value);
        if (value < minValue) {
            minValue = value;
        }
        if (value > maxValue) {
            maxValue = value;
        }
        distinctValues.add(value);

    }

    public double getValue(int index) {
        return values.get(index);
    }

    public String getLabel() {
        return label;
    }
    
}
