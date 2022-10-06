package com.slef.learnjava.stream;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * 有stream 流 再也不用写复杂sql查询了
 * 函数式编程（Functional Programming）是把函数作为基本运算单元，函数可以作为变量，可以接收函数，还可以返回函数。
 * 历史上研究函数式编程的理论是Lambda演算，所以我们经常把支持函数式编程的编码风格称为Lambda表达式。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1255943847278976 《函数式编程》
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1305158055100449
 */
public class StreamDemo {
    public static void main(String[] args) {

        int minInt = Stream.of(10, 20, 30).min(Comparator.comparingInt(Integer::intValue)).get();
        System.out.println("利用Stream获取集合中的最小值:" + minInt);
    }
}
