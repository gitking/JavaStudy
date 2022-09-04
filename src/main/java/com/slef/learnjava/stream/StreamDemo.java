package com.slef.learnjava.stream;

import java.util.Comparator;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {

        int minInt = Stream.of(10, 20, 30).min(Comparator.comparingInt(Integer::intValue)).get();
        System.out.println("利用Stream获取集合中的最小值:" + minInt);
    }
}
