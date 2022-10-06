package com.slef.learnjava.stream.demo;

import java.util.stream.LongStream;

public class Main {

    static long a = 0;
    static long b = 1;

    long c = 2;

    public static void main(String[] args) {
        LongStream fibStream = LongStream.generate(()->{
            long tmp = a;
            a = b;
            b += tmp;
            return a;
        });
        fibStream.limit(20).forEach(System.out::println);
    }

    private void text() {
        LongStream fibStream = LongStream.generate(()->{
            // 这个属性c是怎么传进来的？？？？ TODO
            long tmp = c;
            a = b;
            b += tmp;
            return a;
        });
        fibStream.limit(20).forEach(System.out::println);
    }
}
