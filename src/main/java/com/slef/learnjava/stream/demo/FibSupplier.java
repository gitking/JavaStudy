package com.slef.learnjava.stream.demo;

import java.util.function.LongSupplier;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 编写一个能输出斐波拉契数列（Fibonacci）的LongStream：1, 1, 2, 3, 5, 8, 13, 21, 34, ...
 *
 */
public class FibSupplier implements LongSupplier {

    long n = 0;
    long m = 1;
    long temp = 0;

    /**
     * 每执行一次互换n和l的位置
     * @return
     */
    public long getAsLong() {
        temp = n;
        n = m + n;
        m = temp;
        return n;
    }

    public static void main(String[] args) {
        LongStream longStream = LongStream.generate(new FibSupplier());
        // 生成斐波那契数列
        longStream.limit(20).forEach(System.out::println);


    }
}
