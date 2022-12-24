package com.slef.learnjava.lambda.entity;

import java.util.Comparator;

public class LexicographicComparator implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        System.out.println("调用这里了LexicographicComparator");
        return o1.name.compareToIgnoreCase(o2.name);
    }
}
