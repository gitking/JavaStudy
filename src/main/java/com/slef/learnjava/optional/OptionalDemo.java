package com.slef.learnjava.optional;

import com.slef.learnjava.optional.entity.Person;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * https://mp.weixin.qq.com/s/74RCyxUduRHamiz5qV-V2w 《太Low了，不要用 if (obj != null) 判空了，新姿势，不香吗？》
 * 我们今天就要尽可能的利用Java8的新特性 Optional来尽量简化代码同时高效处理NPE（Null Pointer Exception 空指针异常）
 * 简单来说，Opitonal类就是Java提供的为了解决大家平时判断对象是否为空用 会用 null!=obj 这样的方式存在的判断，
 * 从而令人头疼导致NPE（Null Pointer Exception 空指针异常），同时Optional的存在可以让代码更加简单，可读性跟高，代码写起来更高效.
 * https://juejin.cn/post/6844904154075234318 《都2020年了,还在用if(obj!=null)做非空判断??带你快速上手Optional实战性理解! 》
 */
public class OptionalDemo {
    public static void main(String[] args) {
        Person person = new Person();
        /**
         * Optional类的构造方法都是私有的，我们不能直接new Optional
         */
        Optional.ofNullable(person).orElse(new Person());
        // 第一：创建Optional对象:empty()、of()、ofNullable()
        // 创建一个空的Optional
        Optional<Person> person2  = Optional.empty();

        Optional<Person> person1 = Optional.ofNullable(null);

        // 创建一个Person类型的Optional,注意如果 Optional.of(null)的参数为null,为报空指针异常，this.value = Objects.requireNonNull(value);看Optional的构造方法源码就知道了
        Optional<Person> optionalPerson = Optional.of(new Person());

        // 第二：从Optional对象上面获取我们想要的值:get()、orElse()、orElseGet()、orElseThrow()、
        // 注意：如果optionalPerson的值为null，这个get方法会抛出异常：throw new NoSuchElementException("No value present");
        Person person3 = optionalPerson.get();
        // orElse() 方法，如果optionalPerson的值为null,返回orElse的参数，当做默认值返回
        Person personV = optionalPerson.orElse(new Person());

        // 注意orElseGet的方法是是一个供给型的函数式接口Supplier<? extends T> other,
        // orElseGet()方法的意思是如果optionalPerson的值为null，我就调用你指定的供给型函数式接口给你产生一个新的对象返回给你
        Person person4V = optionalPerson.orElseGet(Person::new);
        // 如果optionalPerson为null就给你抛你自己指定的异常,orElseThrow的方法参数同样是一个供给型的函数式接口：Supplier<? extends X> exceptionSupplier
        // 只不过这个供给型的函数式接口必须是Throwable异常的子类啊，看下源码非常有意思
        /**
         * public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
         *         if (value != null) {
         *             return value;
         *         } else {
         *             throw exceptionSupplier.get();
         *         }
         *     }
         */
        Person personException = optionalPerson.orElseThrow(NullPointerException::new);

        // 第三：判断Optional里面的值是否为null：isPresent()、ifPresent()，注意第二个方法开头是if不是is。
        // 判断optionalPerson的值是否为null，不为null返回true,可以看看源码非常简单
        if (optionalPerson.isPresent()) {
            System.out.println("optionalPerson 不为null");
        } else {
            System.out.println("optionalPerson 为null");
        }

        // 注意这里是ifPresent不是is开头的
        optionalPerson.ifPresent(p -> System.out.println("年龄为:" + p.getAge()));

        Person person4 = new Person();
        person4.setName("过滤演示");
        person4.setAge(10);
        Optional<Person> optionalFilter = Optional.ofNullable(person4);
        // filter方法的参数为：Predicate<? super T> predicate，注意filter方法的参数不能为空，为空会报错Objects.requireNonNull(predicate);
        Optional<Person> filterResult = optionalFilter.filter(person5 -> person5.getAge()>11);
        System.out.println("过滤之后是否有值呢？true还是false" + filterResult.isPresent());

        String nameValue = optionalFilter.map(Person::getName).orElse("Person的name为null,我是默认值返回给你了");
        System.out.println("Optional的map方法" + nameValue);

        // 注意flatMap的返回值还是Optional类型的对象
        Optional<String> optionalFlatMap = optionalFilter.flatMap(person5 -> Optional.ofNullable(person5.getName()));

        System.out.println(optionalFlatMap.orElse("如果optionalFlatMap的值为null，就把我返回出去"));

        // 注意,有坑,Optional只能判断null，不能判断空字符串，比如下面这个例子
        Person person5 = new Person();
        person5.setName("");
        String nameValueRes = "";
        if (StringUtils.isBlank(person5.getName())) {
            nameValueRes = "如果名字为空(null或者空字符串),就是使用我这个默认值";
        }
        System.out.println(nameValueRes);

        nameValueRes = Optional.ofNullable(person5).map(Person::getName).orElse("如果名字为空(null或者空字符串),就是使用我这个默认值");
        System.out.println("注意空字符串对Optional来说不是null:" + nameValueRes);

        /**
         * JDK9,JDK1.9对Optional优化
         * 首先增加了三个方法:
         * or()、ifPresentOrElse() 和 stream()
         * 1. or() 与orElse等方法相似，如果对象不为空返回对象，如果为空则返回or()方法中预设的值。
         * 2. ifPresentOrElse() 方法有两个参数：一个 Consumer 和一个 Runnable。如果对象不为空，会执行 Consumer 的动作，否则运行 Runnable。相比ifPresent（）多了OrElse判断。
         * 3. stream() 将Optional转换成stream，如果有值就返回包含值的stream，如果没值，就返回空的stream。
         * 4. 因为这个jdk1.9的Optional具体我没有测试，同时也发现有蛮好的文章已经也能让大家明白jdk1.9的option的优化,我就不深入去说了。
         */

    }
}
