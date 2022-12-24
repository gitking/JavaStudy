package com.slef.learnjava.optional;

import java.util.Optional;

/**
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1337645544243233 《NullPointerException》 廖雪峰
 *
 * 好的编码习惯可以极大地降低NullPointerException的产生，例如：
 * 成员变量在定义时初始化：
 * public class Person {
 *     private String name = "";
 * }
 * 使用空字符串""而不是默认的null可避免很多NullPointerException，编写业务逻辑时，用空字符串""表示未填写比null安全得多。
 * 返回空字符串""、空数组而不是null：
 * public String[] readLinesFromFile(String file) {
 *     if (getFileSize(file) == 0) {
 *         // 返回空数组而不是null:
 *         return new String[0];
 *     }
 *     ...
 * }
 * 这样可以使得调用方无需检查结果是否为null。
 *
 * 如果产生了NullPointerException，例如，调用a.b.c.x()时产生了NullPointerException，原因可能是：
 * a是null；
 * a.b是null；
 * a.b.c是null；
 * 确定到底是哪个对象是null以前只能打印这样的日志：
 * System.out.println(a);
 * System.out.println(a.b);
 * System.out.println(a.b.c);
 * 从Java 14开始，如果产生了NullPointerException，JVM可以给出详细的信息告诉我们null对象到底是谁。我们来看例子：
 * 可以在NullPointerException的详细信息中看到类似... because "<local1>.address.city" is null，意思是city字段为null，这样我们就能快速定位问题所在。
 * 这种增强的NullPointerException详细信息是Java 14新增的功能，但默认是关闭的，我们可以给JVM添加一个-XX:+ShowCodeDetailsInExceptionMessages参数启用它：
 * java -XX:+ShowCodeDetailsInExceptionMessages Main.java
 */
public class NullPointerExceptionDemo {

    /**
     * 如果调用方一定要根据null判断，比如返回null表示文件不存在，那么考虑返回Optional<T>：
     * 这样调用方必须通过Optional.isPresent()判断是否有结果。
     * @param file
     * @return
     */
    public Optional<String> readFromFile(String file) {
        if (file == null) {
            // 这样调用方必须通过Optional.isPresent()判断是否有结果。
            Optional.empty();
        }
        return Optional.ofNullable("");
    }
}
