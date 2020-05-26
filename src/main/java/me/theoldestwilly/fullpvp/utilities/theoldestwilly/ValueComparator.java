package me.theoldestwilly.fullpvp.utilities.theoldestwilly;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator {
    Map map;

    public ValueComparator(Map map) {
        this.map = map;
    }

    public int compare(Object keyA, Object keyB) {
        Comparable valueA = (Comparable)this.map.get(keyA);
        Comparable valueB = (Comparable)this.map.get(keyB);
        return valueB.compareTo(valueA);
    }
}
