package com.slef.learnjava.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 创建Stream
 * 要使用Stream，就必须先创建它。创建Stream有很多种方法，我们来一一介绍。
 * Stream.of()
 * 创建Stream最简单的方式是直接用Stream.of()静态方法，传入可变参数即创建了一个能输出确定元素的Stream：
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1322655160467490
 */
public class CreateStream {

    public static void main(String[] args) throws IOException {
        Stream<String> stream = Stream.of("A", "B", "C", "D");
        // forEach()方法相当于内部循环调用
        // 可传入符合Consumer接口的void accept(T t)的方法引用
        // 虽然这种方式基本上没啥实质性用途，但测试的时候很方便。
        stream.forEach(System.out::println);

        // 创建Stream方式二:基于数组或Collection
        // 第二种创建Stream的方法是基于一个数组或者Collection，这样该Stream输出的元素就是数组或者Collection持有的元素：
        // 把数组变成Stream使用Arrays.stream()方法。对于Collection（List、Set、Queue等），直接调用stream()方法就可以获得Stream。
        // 上述创建Stream的方法都是把一个现有的序列变为Stream，它的元素是固定的。
        Stream<String> stream1 = Arrays.stream(new String[]{"A", "B", "C"});
//        Stream<String> stream2 = List.of("X", "Y", "Z").stream();
        List<String> stringList = new ArrayList<>();
        stringList.add("X");
        stringList.add("Y");
        stringList.add("Z");
        Stream<String> stream2 = stringList.stream();
        stream1.forEach(System.out::println);
        stream2.forEach(System.out::println);

        // 创建Stream方式三:基于Supplier
        /*
            创建Stream还可以通过Stream.generate()方法，它需要传入一个Supplier对象：
            Stream<String> s = Stream.generate(Supplier<String> sp);
            基于Supplier创建的Stream会不断调用Supplier.get()方法来不断产生下一个元素，这种Stream保存的不是元素，而是算法，它可以用来表示无限序列。
            例如，我们编写一个能不断生成自然数的Supplier，它的代码非常简单，每次调用get()方法，就生成下一个自然数：
            Supplier:供应商的意思
         */
        Stream<Integer> natual = Stream.generate(new NatualSupplier());
        // 注意：无限序列必须先变成有限序列再打印
        natual.limit(20).forEach(System.out::println);

        // 通过迭代创建流
        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2).limit(20);
        System.out.println("通过迭代创建流");
        stream3.forEach(System.out::println);

        // 获取一个并行流
        Stream<String> parallelStream = stringList.parallelStream();

        /*
         * 上述代码我们用一个Supplier<Integer>模拟了一个无限序列（当然受int范围限制不是真的无限大）。如果用List表示，即便在int范围内，也会占用巨大的内存，而Stream几乎不占用空间，因为每个元素都是实时计算出来的，用的时候再算。
         * 对于无限序列，如果直接调用forEach()或者count()这些最终求值操作，会进入死循环，因为永远无法计算完这个序列，所以正确的方法是先把无限序列变成有限序列，例如，用limit()方法可以截取前面若干个元素，这样就变成了一个有限序列，对这个有限序列调用forEach()或者count()操作就没有问题。
         */

        // 其他方法：创建Stream的第三种方法是通过一些API提供的接口，直接获得Stream。
        // 例如，Files类的lines()方法可以把一个文件变成一个Stream，每个元素代表文件的一行内容：
        try (Stream<String> lines = Files.lines(Paths.get("/path/to/file.txt"))){
            // 此方法对于按行遍历文本文件十分有用。
        }

        // 另外，正则表达式的Pattern对象有一个splitAsStream()方法，可以直接把一个长字符串分割成Stream序列而不是数组：
        Pattern p = Pattern.compile("\\s+");
        Stream<String> s = p.splitAsStream("The qucik brown fox jumps over the lazy dog");
        s.forEach(System.out::println);

        // 基本类型
        // 因为Java的范型不支持基本类型，所以我们无法用Stream<int>这样的类型，会发生编译错误。为了保存int，只能使用Stream<Integer>，但这样会产生频繁的装箱、拆箱操作。
        // 为了提高效率，Java标准库提供了IntStream、LongStream和DoubleStream这三种使用基本类型的Stream，它们的使用方法和范型Stream没有大的区别，设计这三个Stream的目的是提高运行效率：
        // 将int[]数组变为IntStream:
        IntStream is = Arrays.stream(new int[]{1, 2, 3});

        // 将Stream<String> 转换为LongStream
//        LongStream ls = List.of("1", "2", "3").stream().mapToLong(Long::parseLong);
        List<String> stringList1 = new ArrayList<>();
        stringList1.add("1");
        stringList1.add("2");
        stringList1.add("3");

        LongStream ls = stringList1.stream().mapToLong(Long::parseLong);
    }
}
