package com.slef.learnjava.stream;

import java.util.function.Supplier;

/**
 * Supplier:供应商的意思
 *
 */
public class NatualSupplier implements Supplier<Integer> {
    int n = 0;

    public Integer get() {
        n++;
        return n;
    }
}
