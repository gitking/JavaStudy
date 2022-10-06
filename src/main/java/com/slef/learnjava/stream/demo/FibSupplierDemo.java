package com.slef.learnjava.stream.demo;

import java.util.function.LongSupplier;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class FibSupplierDemo implements LongSupplier {

    long n = 0;

    @Override
    public long getAsLong() {
        n++;
        return fib(n);
    }

    long fib(long n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) {
        LongStream stream = LongStream.generate(new FibSupplierDemo());
        stream.limit(20).forEach(System.out::println);
    }
}
