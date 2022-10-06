package com.slef.learnjava.stream.demo;

import java.util.function.LongSupplier;
import java.util.stream.LongStream;

public class FibSupplierStream implements LongSupplier {

    long i = 0;
    long j = 1;

    @Override
    public long getAsLong() {
        long tmp = i + j;
        i = j;
        j = tmp;
        return i;
    }

    public static void main(String[] args) {
        LongStream ls = LongStream.generate(new FibSupplierStream());
        ls.limit(20).forEach(System.out::println);

        LongStream.generate(new LongSupplier() {
            long a = 0, b= 1;


            @Override
            public long getAsLong() {
                long tmp = b;
                b = a + b;
                return a = tmp;
            }
        }).limit(10).forEach(System.out::println);

        // 优雅永不过时
        System.out.println("优雅永不过时");
        LongStream.generate(new LongSupplier() {
            long a = 1, b = 0;
            @Override
            public long getAsLong() {
                // 不考虑可读性还可以更简短一点
                // https://www.liaoxuefeng.com/wiki/1252599548343744/1322655160467490#0
                // emmm,看了一下编译出来的字节码，编译器好像没我想的那么智能.......
                //
                //我建议还是直接这么写，可读性以后还是用注释保证吧。
                //
                //public long getAsLong() {
                //
                //    return b = a + (a = b);
                //
                //}
                return b = a + (a = b);
            }
        }).limit(20).forEach(System.out::println);
    }
}
