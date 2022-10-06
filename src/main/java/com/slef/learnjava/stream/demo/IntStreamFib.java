package com.slef.learnjava.stream.demo;

import java.util.function.IntSupplier;
import java.util.stream.IntStream;

public class IntStreamFib implements IntSupplier {

    private int a = 0;
    private int b = 0;
    private int c = 1;
    @Override
    public int getAsInt() {
        a = b;
        b = c;
        c = a + b;
        return b;
    }

    public static void main(String[] args) {
        IntStream fib = IntStream.generate(new IntStreamFib());
        fib.limit(30).forEach(System.out::println);
    }
}
