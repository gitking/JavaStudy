package com.slef.learnjava.lambda;

import com.slef.learnjava.lambda.entity.LexicographicComparator;
import com.slef.learnjava.lambda.entity.Person;

import java.util.*;

/**
 * https://www.jianshu.com/p/81e5c3e88fc6 《Java中Comparator的使用》
 * Comparable与Comparator的区别
 * Comparable & Comparator都是用来实现集合中元素的比较、排序的，只是 Comparable 是在集合内部定义的方法实现的排序，Comparator 是在集合外部实现的排序，
 * 所以，如想实现排序，就需要在集合外定义Comparator接口的方法或在集合内实现 Comparable接口的方法，Comparator位于包java.util下，而Comparable位于包java.lang下。
 *
 * Java中有两种方式来提供比较功能。第一种是实现java.lang.Comparable接口，使你的类天生具有比较的能力，此接口很简单，只有一个compareTo一个方法。
 * 此方法接收另一个Object为参数，如果当前对象小于参数则返回负值，如果相等则返回零，否则返回正值，也就是：
 * x.compareTo(y) 来“比较x和y的大小”。若返回“负数”，意味着“x比y小”；返回“零”，意味着“x等于y”；返回“正数”，意味着“x大于y”。
 *
 * 作者：Fighting_rain
 * 链接：https://www.jianshu.com/p/81e5c3e88fc6
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class ComparatorDemo {

    public static void main(String[] args) {

        // 使用Comparable比较的例子：
        List<Person> list = new ArrayList<>();
        list.add(new Person("aaa"));
        list.add(new Person("ddd"));
        list.add(new Person("bbb"));
        list.add(new Person("ccc"));
 
        // 这里会自动调用Person中重写的compareTo方法
        Collections.sort(list);
        list.forEach(a -> System.out.println(a.getName()));

        // 使用Comparator比较的例子：
        List<Person> personList = Arrays.asList(
                new Person("Job"),
                new Person("Pete"),
                new Person("Chirs"),
                new Person("中文排序TODO")
        );

        Collections.sort(personList, new LexicographicComparator());
        personList.forEach(a -> System.out.println(a.getName()));

        Collections.sort(personList, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }
}
