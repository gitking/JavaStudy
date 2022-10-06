package com.slef.learnjava.stream.demo;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NatualSupplier implements Supplier<Integer> {

    private final int[] temp = new int[]{1, 1, 0};
    private int index = -1;

    @Override
    public Integer get() {
//        int s = temp[0]=temp[1]+temp[2];
        index++;
        if (index < 2) {
            return temp[index];
        }
        return (temp[index % 3] = temp[(index - 1) % 3] + temp[(index - 2) % 3]);
    }

    public static void main(String[] args) {
        Stream<Integer> si = Stream.generate(new NatualSupplier());
        si.limit(20).forEach(System.out::println);
    }
}
