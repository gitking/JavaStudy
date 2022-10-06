package com.slef.learnjava.lambda;

import com.slef.learnjava.lambda.entity.Doctor;
import com.slef.learnjava.lambda.entity.Person;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 你应该知道的Java8新特性之Lambda表达式 https://juejin.cn/post/7148334544755064846
 *
 * Lambda表达式是一个匿名函数，我们可以把 Lambda 表达式理解为是一段可以传递的代码（将代码 像数据一样进行传递）。
 * 可以写出更简洁、更灵活的代码。作为一种更紧凑的代码风格，使Java的语言表达能力得到了提升。
 *
 * 格式:
 *  '->' :lambda操作符或箭头操作符
 *  '->'的左边: Lambda形参列表(其实就是接口中的抽象方法的形参列表)
 *  '->'的右边: Lambda体 (其实就是 重写的抽象方法的方法体)
 *
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1305207799545890
 *  方法引用
 *  使用Lambda表达式，我们就可以不必编写FunctionalInterface接口的实现类，从而简化代码：
 * Arrays.sort(array, (s1, s2) -> {
 *     return s1.compareTo(s2);
 * });
 * 实际上，除了Lambda表达式，我们还可以直接传入方法引用。例如：
 *
 * public class Main {
 *     public static void main(String[] args) {
 *         String[] array = new String[] { "Apple", "Orange", "Banana", "Lemon" };
 *         Arrays.sort(array, Main::cmp);
 *         System.out.println(String.join(", ", array));
 *     }
 *
 *     static int cmp(String s1, String s2) {
 *         return s1.compareTo(s2);
 *     }
 * }
 * 上述代码在Arrays.sort()中直接传入了静态方法cmp的引用，用Main::cmp表示。
 * 因此，所谓方法引用，是指如果某个方法签名和接口恰好一致，就可以直接传入方法引用。
 * 因为Comparator<String>接口定义的方法是int compare(String, String)，和静态方法int cmp(String, String)相比，除了方法名外，方法参数一致，返回类型相同，因此，我们说两者的方法签名一致，可以直接把方法名作为Lambda表达式传入：
 * 注意：在这里，方法签名只看参数类型和返回类型，不看方法名称，也不看类的继承关系。
 *
 * 总结 方法引用就四种情况
 * 1. 对象的引用 :: 实例方法名
 * 2. 类名 :: 静态方法名
 * 3. 类名 :: 实例方法名
 * 4.构造器引用
 * 补充,所谓引用 就是避免写重复代码,理解这一层,感觉简单多了
 *
 * @date 2022/10/02
 */
public class LambdaDemo {

    public static void main(String[] args) {
        /**
         * 2. Lambda表达式的使用
         * Lambda表达式的使用为了方便理解，分为6种情况介绍，通过内置函数式接口来举例，也可以对比匿名函数来理解。
         */

        // 语法格式一：无参，无返回值，Lambda体只需一条语句
        Runnable r2 = () -> {System.out.println("Test1");};

        // 语法格式二：Lambda需要一个参数，并且无返回值
        Consumer<String> con2 = (String s) -> { System.out.println(s); };
        con2.accept("test2");

        Consumer<String> con22 = (s) ->  System.out.println(s);
        con22.accept("test22，注意这种呢写法把String参数类型省略了，但是小括号没省略()");

        // 语法格式三：Lambda只需要一个参数时，参数的小括号可以省略，同时省略参数变量类型（类型推断）
        Consumer<String> con3 = s -> {
            System.out.println(s);
            System.out.println("Lambda类型推断");
        };
        con3.accept("test2");

        Consumer<String> con33 = s -> System.out.println(s);
        con33.accept("大括号都可以省略了");

        // 语法格式四：Lambda需要两个参数，并且有返回值
        Comparator<Integer> com2 = (Integer o1, Integer o2) -> {
            return o1.compareTo(o2);
        };

        // 语法格式五：当 Lambda 体只有一条语句时，return 与大括号可以省略
        Comparator<Integer> com3 = (Integer o1, Integer o2) -> o1.compareTo(o2);

        Comparator<Integer> com34 = (o1, o2) -> {
            System.out.println("注意这种写法把Integer参数类型省略了,但是小括号没有省略,并且这里面还能写System.out.println语句");
            return o1.compareTo(o2);
        };

        // 语法格式五：数据类型可以省 略，因为可由编译器推断得出， 称为“类型推断
        // 数据类型可以省略，因为可由编 译器推断得出， 称为“类型推断
        Comparator<Integer> com4 = (o1,o2) -> o1.compareTo(o2);

        // 上述 Lambda 表达式中的参数类型都是由编译器推断得出的。Lambda 表达式中无需指定类型，程序依然可以编译，这是因为 javac 根据程序的上下文，在后台推断出了参数的类型。Lambda 表达式的类型依赖于上下文环境，是由编译器推断出来的。这就是所谓的“类型推断”
        //
        //作者：小野学Java
        //链接：https://juejin.cn/post/7148334544755064846
        //来源：稀土掘金
        //著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

        /**
         * 3. 函数式接口
         * 3.1 什么是函数式接口
         * 只包含一个抽象方法的接口，称为函数式接口。
         * 你可以通过 Lambda 表达式来创建该接口的对象。（若 Lambda 表达式抛出一个受检异常，那么该异常需要在目标接口的抽象方法上进行声明）。Lambda表达式也是依赖于函数式接口的。
         * 我们可以在任意函数式接口上使用 @FunctionalInterface 注解， 这样做可以检查它是否是一个函数式接口，同时javadoc也会包含一条声明，说明这个接口是一个函数式接口。
         * 3.2 如何理解函数式接口
         * 1.Java从诞生口起就是一直倡导“一切皆对象”，在Java里面面向对象(OOP)编程是一切。但是随着python、 scala等语言的兴起和新技术的挑战，Java不得不做出调整以便支持更加广泛的技术要求，也即Java不但可以支持OOP还可以支持OOF(面向函数编程)
         * 2.在函数式编程语言当中，函数被当做一等公民对待。在将函数作为一等公民的编程语言中，Lambda表达式的类型是函数。但是在Java8中，有所不同。在Java8中，Lambda表达式是对象，而不是函数，它们必须依附于一类特别的对象类型-函数式接口。
         * 3.简单的说，在Java8中，Lambda表达式就是一个函数式接口的实例。这就是Lambda表达式和函数式接口的关系。也就是说，只要一少对象是函数式接口的实例，那么该对象就可以用Lambda表达式来表示。
         * 所以以前用匿名实现类表示的现在都可以用Lambda表达式来写。
         *
         * Java 内置四大核心函数式接口
         * 函数式接口            参数类型            返回类型            用途
         * Consumer 消费型接口   T                   void        对类型为T的对象应用操 作，包含方法：void accept(T t)
         * Supplier供给型接口    无                   T           返回类型为T的对象，包含方法：T get();
         * Function<T,R>函数型接口 T                 R           对类型为T的对象应用操 作，并返回结果。结果 是R类型的对象。包含方 法：R apply(T t);
         * Predicate断定型接口   T                 boolean       确定类型为T的对象是否 满足某约束，并返回 boolean 值。包含方法 boolean test(T t);
         *
         * 作者：小野学Java
         * 链接：https://juejin.cn/post/7148334544755064846
         * 来源：稀土掘金
         * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
         */

        consumeMethod(500, new Consumer<Integer>(){
            @Override
            public void accept(Integer integer) {
                System.out.println("匿名函数的用法示例：您一共消费了:" + integer);
            }
        });

        // 语法格式三：Lambda只需要一个参数时，参数的小括号可以省略，同时省略参数变量类型（类型推断）
        consumeMethod(500, money-> System.out.println("lambda表达式用法示例：您一共消费了:" + money));

        List<String> list = Arrays.asList("sss", "rrr", "yyyyy", "ssdasds","sd");
        // 匿名函数的用法示例
        list = predit(list, new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.length() > 3;
            }
        });
        System.out.println(list.toString());

        // lambda表达式写法
        List<String> list1 = Arrays.asList("12", "13", "14", "15", "16", "17");

        // 语法格式三：Lambda只需要一个参数时，参数的小括号可以省略，同时省略参数变量类型（类型推断）
        list1 = predit(list1, i->Integer.parseInt(i)>14);
        System.out.println(list1.toString());

        /**
         * https://www.liaoxuefeng.com/wiki/1252599548343744/1305158055100449
         * Lambda表达式
         * 在Java程序中，我们经常遇到一大堆单方法接口，即一个接口只定义了一个方法：
         * Comparator
         * Runnable
         * Callable
         * 以Comparator为例，我们想要调用Arrays.sort()时，可以传入一个Comparator实例，以匿名类方式编写如下：
         */
        String[] array = new String[] {"Apple", "Orange", "Banana", "Lemon"};
        Arrays.sort(array, new Comparator<String>() {
            // 匿名函数
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

        // 上述写法非常繁琐。从Java 8开始，我们可以用Lambda表达式替换单方法接口。改写上述代码如下：
        Arrays.sort(array, (s1, s2) -> {
            return s1.compareTo(s2);
        });
        // 如果只有一行retrun xxx的代码,完全可以用更简单的写法
        Arrays.sort(array, (s1, s2) -> s1.compareTo(s2));
        Arrays.sort(array, Comparator.naturalOrder());
        Arrays.sort(array, String::compareTo);

        // 忽略大小写排序
        Arrays.sort(array, Comparator.comparing(String::toLowerCase));
        Arrays.sort(array, String::compareToIgnoreCase);

        System.out.println("排序后的数组:" + String.join(",", array));
        /*
            观察Lambda表达式的写法，它只需要写出方法定义：
            (s1, s2) -> {
                return s1.compareTo(s2);
            }
            其中，参数是(s1, s2)，参数类型可以省略，因为编译器可以自动推断出String类型。
            -> { ... }表示方法体，所有代码写在内部即可。Lambda表达式没有class定义，因此写法非常简洁。
            如果只有一行return xxx的代码，完全可以用更简单的写法：
            Arrays.sort(array, (s1, s2) -> s1.compareTo(s2));
            返回值的类型也是由编译器自动推断的，这里推断出的返回值是int，因此，只要返回int，编译器就不会报错。
         */

        /**
         * 使用Lambda表达式，我们就可以不必编写FunctionalInterface接口的实现类，从而简化代码：
         */
        Arrays.sort(array, (s1, s2)->s1.compareTo(s2));

        // 实际上，除了Lambda表达式，我们还可以直接传入方法引用。例如：
        Arrays.sort(array, LambdaDemo::cmp);
        System.out.println("::双冒号，俩个冒号代表方法引用，排序后的数组为:" + String.join(",", array));

        // System.out.println("注意Main(LambdaDemo)类里面的compareTo方法不是静态方法,是实例方法，并且有俩个参数");
        Arrays.sort(array, new LambdaDemo()::compareTo);
        System.out.println("注意上面这行代码,是new LambdaDemo()::compareTo，参见评论：https://www.liaoxuefeng.com/wiki/1252599548343744/1305207799545890");

        /**
         * 上述代码在Arrays.sort()中直接传入了静态方法cmp的引用，用Main::cmp表示。
         * 因此，所谓方法引用，是指如果某个方法签名和接口恰好一致，就可以直接传入方法引用。
         * 因为Comparator<String>接口定义的方法是int compare(String, String)，
         * 和静态方法int cmp(String, String)相比，除了方法名外，方法参数一致，返回类型相同，因此，我们说两者的方法签名一致，可以直接把方法名作为Lambda表达式传入：
         *
         * 注意：在这里，方法签名只看参数类型和返回类型，不看方法名称，也不看类的继承关系。
         */

        Arrays.sort(array, String::compareTo);
        System.out.println("引用实例方法，排序后的数组为:" + String.join(",", array));
        /**
         * 我们再看看如何引用实例方法。如果我们把代码改写如下：
         * 不但可以编译通过，而且运行结果也是一样的，这说明String.compareTo()方法也符合Lambda定义。
         * 观察String.compareTo()的方法定义：
         *
         * public final class String {
         *     public int compareTo(String o) {
         *
         *     }
         * }
         * 这个方法的签名只有一个参数，为什么和int Comparator<String>.compare(String, String)能匹配呢？
         * 因为实例方法有一个隐含的this参数，String类的compareTo()方法在实际调用的时候，第一个隐含参数总是传入this，相当于静态方法：
         * public static int compareTo(this, String o);
         * 所以，String.compareTo()方法也可作为方法引用传入。
         */

        // 构造方法引用

        /*
          除了可以引用静态方法和实例方法，我们还可以引用构造方法。
          我们来看一个例子：如果要把一个List<String>转换为List<Person>，应该怎么办？
          List<String> names = List.of("Bob", "Alice", "Tim");
          List<Person> person = ???
          传统的做法是先定义一个ArrayList<Person>，然后用for循环填充这个List：
            List<String> names = List.of("Bob", "Alice", "Tim");
            List<Person> persons = new ArrayList<>();
            for (String name : names) {
                persons.add(new Person(name));
            }
            要更简单地实现String到Person的转换，我们可以引用Person的构造方法：
         */
//        List<String> names = List.of("Bob", "Alice", "Tim");
        List<String> names = new ArrayList<>();
        names.add("Bob");
        names.add("Alice");
        names.add("Tim");
        List<Person> personList = names.stream().map(Person::new).collect(Collectors.toList());
        System.out.println("List<String>转换成List<Person>" + personList);
        /**
         * 后面我们会讲到Stream的map()方法。现在我们看到，这里的map()需要传入的FunctionalInterface的定义是：
         * @FunctionalInterface
         * public interface Function<T, R> {
         *     R apply(T t);
         * }
         * 把泛型对应上就是方法签名Person apply(String)，即传入参数String，返回类型Person。而Person类的构造方法恰好满足这个条件，因为构造方法的参数是String，
         * 而构造方法虽然没有return语句，但它会隐式地返回this实例，类型就是Person，因此，此处可以引用构造方法。构造方法的引用写法是类名::new，因此，此处传入Person::new。
         */


        /**
         * 参考之前的笔记补充一下吧
         * 1. 对象的引用 :: 实例方法名
         * 示例一
         */
        PrintStream ps = System.out;
        // 相当于创建了一个匿名类了
        Consumer<String> con = (str) -> ps.println(str);
        con.accept("使用System.out进行打印");
        System.out.println("----------------------");

        Consumer<String> con21 = ps::println;
        con21.accept("方法引用创建对象");

        /**
         * 1. 对象的引用 :: 实例方法名
         * 示例二
         * Employee emp = new Employee(101, "张三", 18, 9999.99);
         *  Supplier<String> sup = () -> emp.getName();
         *  System.out.println(sup.get());
         *  System.out.println("----------------------------------");
         *  Supplier<String> sup2 = emp::getName;
         *  System.out.println(sup2.get());
         */

        /**
         * 2. 类名 :: 静态方法名
         *
         * 示例一
         *
         * Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
         *
         *  System.out.println("-------------------------------------");
         *
         *  Comparator<Integer> com2 = Integer::compare;
         *
         * 示例二
         *
         * BiFunction<Double, Double, Double> fun = (x, y) -> Math.max(x, y);
         *
         *  System.out.println(fun.apply(1.5, 22.2));
         *
         *  System.out.println("--------------------------------------------------");
         *
         *  BiFunction<Double, Double, Double> fun2 = Math::max;
         *
         *  System.out.println(fun2.apply(1.2, 1.5));
         *
         *  3. 类名 :: 实例方法名
         *
         * 示例一
         *
         * BiPredicate<String, String> bp = (x, y) -> x.equals(y);
         *
         *  System.out.println(bp.test("abcde", "abcde"));
         *
         *  System.out.println("-----------------------------------------");
         *
         *  BiPredicate<String, String> bp2 = String::equals;
         *
         *  System.out.println(bp2.test("abc", "abc"));
         *
         * 注意
         *
         * ①方法引用所引用的方法的参数列表与返回值类型，需要与函数式接口中抽象方法的参数列表和返回值类型保持一致！
         *
         * ②若Lambda 的参数列表的第一个参数，是实例方法的调用者，第二个参数(或无参)是实例方法的参数时，格式：
         *
         * ClassName::MethodName
         *
         * 以上不包含构造方法
         * 总结 方法引用就四种情况
         * 1. 对象的引用 :: 实例方法名
         * 2. 类名 :: 静态方法名
         * 3. 类名 :: 实例方法名
         * 4.构造器引用
         * 补充,所谓引用 就是避免写重复代码,理解这一层,感觉简单多了
         * https://www.liaoxuefeng.com/wiki/1252599548343744/1305207799545890#0
         */
        BiFunction<Double, Double, Double> fun2 = Math::max;

        /*

        好像 还有个 数组 引用 贴下 代码
类型[] :: new;
@Testpublic void test8() {
 String[] strings = new String[8];
 String[] strings1 = {};
 String[] strings2 = {"aaa", "bbb"};
 Function<Integer, String[]> fun = (args) -> new String[args];
 String[] strs = fun.apply(10);
 System.out.println(strs.length);
 System.out.println("--------------------------");
 Function<Integer, Employee[]> fun2 = Employee[]::new; Employee[] emps = fun2.apply(20);
 System.out.println(emps.length);}
         */

        /**
         * 关于函数带不带new，个人见解
         * 哔哩哔哩bili created at June 15, 2020 2:10 PM, Last updated at June 15, 2020 2:10 PM
         * class Test{
         *
         * private int data;
         *
         * public Test(int data) {
         *
         * // TODO Auto-generated constructor stub
         *
         * this.data=data;
         *
         *    }
         *
         * public static void print(int x) {
         *
         *       System.out.printf(""+(x*3));
         *
         *    }
         *
         * public int compare(Test test) {
         *
         * return this.data-test.data;
         *
         *    }
         *
         * public int compare(Test test1,Test test2) {
         *
         * return test1.data-test2.data;
         *
         *    }
         *
         * public String toString() {
         *
         * return ""+data+"";
         *
         *    }
         *
         * }
         *
         * Test[] tests=new Test[10];
         *
         * for(int i = 0; i < tests.length; i++) {
         *
         * tests[i]=newTest(9-i);
         *
         * }
         *
         * for(Test test : tests) {
         *
         * System.out.print(test);
         *
         * }
         *
         * System.out.println();
         *
         * //Arrays.sort(tests,Test::compare);//这时传入的参数是让this对象去调的即调用对象与待比较对象比较，所以暗含一个this。
         *
         * //若果传入new Test(0)::compare则需要两个参数，因为此时这个函数不属于调用对象了，new Test(0)只是一个工具人没有其他用处
         *
         * Arrays.sort(tests,newTest(0)::compare);//这样传入的是实例函数，与上面做区分
         *
         * for(Test test : tests) {
         *
         * System.out.print(test);
         *
         * }
         * System.out.println();
         * Arrays.sort(tests,newTest(0)::compare);
         * Arrays.sort(tests,Test::compare);
         * 两个函数功能完全一样，但是调用逻辑不一样
         * https://www.liaoxuefeng.com/wiki/1252599548343744/1305207799545890#0
         */
    }

    public static int cmp (String s1, String s2) {
        return s1.compareTo(s2);
    }

    public int compareTo(String s1, String s2) {
        System.out.println("注意Main(LambdaDemo)类里面的compareTo方法不是静态方法,是实例方法，并且有俩个参数");
        return s1.compareTo(s2);
    }
    public static void consumeMethod(int money, Consumer<Integer> consumer) {
        consumer.accept(money);
    }

    public static List<String> predit(List<String> strings, Predicate<String> predicate) {
        List<String> list = new ArrayList<>();
        for (String s: strings) {
            if (predicate.test(s)) {
                list.add(s);
            }
        }
        return list;
    }
}
