package com.slef.learnjava.lambda.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1305158055100449 廖雪峰
 * 使用Lambda表达式实现忽略大小写排序
 *
 * 函数式编程，就是可以将函数作为参数的编程方式，Java在引入Lambda表达式之前，无法直接实现函数式编程，都是通过对象间接地将函数引入，很多时候我们只需要函数本身，并不需要对象，这种写法就显得比较繁琐(参考匿名内部类写法)。
 * 有了Lambda表达式之后，我们可以直接将函数作为参数引入了，我们可以直接引入 ()->{} 这样的函数，更精简的方式是引入函数引用(函数引用用 :: 符号表示)。总而言之，Lambda表达式简化了Java的函数式编程。以上是我的一点心得，如有纰漏还请指正。
 *
 * Arrays.sort()的第二个参数其实就是一个函数，而Java本身又没函数的概念，用接口去实现函数，相当于在函数头上盖个盖子，有点多此一举。
 * 这个lambda其实是一个匿名函数。如果函数体小的话，用匿名函数挺方便的。但如果函数体长，那就只能定义函数接口的实现类了。想不明白Java为何不增加函数的定义。
 *
 * static类型方法相当于函数
 */
public class IgnoreCase {

    public static void main(String[] args) {
        String[] arr = new String[]{"C", "B", "A", "d"};
        Arrays.sort(arr, (s1, s2) -> {
            return s1.compareToIgnoreCase(s2);
        });
        System.out.println("方法一：忽略大小写排序：" + Arrays.toString(arr));

        Arrays.sort(arr, (s1, s2) -> s1.toLowerCase().compareTo(s2.toLowerCase()));
        System.out.println("方法二: 忽略大小写排序:" + String.join("&", arr));

        Arrays.sort(arr, Comparator.comparing(String::toLowerCase));
        System.out.println("方法三:" + String.join("-", arr));

        Arrays.sort(arr, String::compareToIgnoreCase);
        System.out.println("方法四:compareToIgnoreCase忽略大小写排序:" + String.join("跟各方", arr));
    }
}
