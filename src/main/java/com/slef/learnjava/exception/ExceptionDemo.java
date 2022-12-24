package com.slef.learnjava.exception;

import java.util.InputMismatchException;

/**
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1264738764506656#0 《抛出异常》 廖雪峰
 * https://blog.csdn.net/Thumb_/article/details/120257574 《java异常：异常链简介》 CSDN _卷心菜_
 *
 */
public class ExceptionDemo {

    public static void main(String[] args) {
        try {
            process1(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            process3(null);
        } catch (Exception e) {
            e.printStackTrace();
            Throwable e1 = e.getCause();
            System.out.println(e1);
            Throwable cause = e1.getCause();
            System.out.println(cause);
            System.out.println("在代码中获取原始异常可以使用Throwable.getCause()方法。如果返回null，说明已经是“根异常”了。");
        }

        try {
            // 被屏蔽的异常
            suppressedException();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 被屏蔽的异常01,当catch和finally都抛出了异常时，虽然catch的异常被屏蔽了，但是，finally抛出的异常仍然包含了它：
            // 通过Throwable.getSuppressed()可以获取所有的Suppressed Exception。
            suppressedException01();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 注意 NullPointerException 并没有 Throwable 类型的构造方法。。。并不是所有异常都可以找根源
            // 可以使用使用initCause()。解决
            process4();
        } catch (Exception e) {
            System.out.println("注意 NullPointerException 并没有Throwable 类型的构造方法。。。并不是所有异常都可以找根源");
            e.printStackTrace();
            System.out.println(e.getCause());
        }


        try {
            // 注意 NullPointerException 并没有 Throwable 类型的构造方法。。。并不是所有异常都可以找根源
            // 可以使用使用initCause()。解决
            process44();
        } catch (Exception e) {
            System.out.println("注意 NullPointerException 并没Throwable 类型的构造方法。。。并不是所有异常都可以找根源,可以使用使用initCause()。解决");
            e.printStackTrace();
            System.out.println(e.getCause());
        }

        testStart();

        testStart01();


    }

    /**
     * 如果一个方法捕获了某个异常后，又在catch子句中抛出新的异常，就相当于把抛出的异常类型“转换”了：
     * 这样会丢失最原始的异常信息，对于排错非常不利。
     * 这样打出的异常栈为：
     * Exception in thread "main" java.lang.IllegalArgumentException
     * 	at com.slef.learnjava.exception.ExceptionDemo.process1(ExceptionDemo.java:23)
     * 	at com.slef.learnjava.exception.ExceptionDemo.main(ExceptionDemo.java:10)
     * 从这个异常栈上面根本看不到原始的空指针异常信息
     * 这说明新的异常丢失了原始异常信息，我们已经看不到原始异常NullPointerException的信息了。
     * 为了能追踪到完整的异常栈，在构造异常的时候，把原始的Exception实例传进去，新的Exception就可以持有原始Exception信息。对上述代码改进如下： process3()
     * @param s
     */
    public static void process1(String s) {
        try {
            process2(s);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
    }

    public static void process2(String s) {
        if (s == null){
            throw new NullPointerException();
        }
    }

    /**
     * 为了能追踪到完整的异常栈，在构造异常的时候，把原始的Exception实例传进去，新的Exception就可以持有原始Exception信息。对上述代码改进如下：
     * 运行process3(null)，打印出的异常栈类似：
     *  java.lang.IllegalArgumentException: java.lang.NullPointerException
     * 	at com.slef.learnjava.exception.ExceptionDemo.process3(ExceptionDemo.java:58)
     * 	at com.slef.learnjava.exception.ExceptionDemo.main(ExceptionDemo.java:17)
     * Caused by: java.lang.NullPointerException
     * 	at com.slef.learnjava.exception.ExceptionDemo.process2(ExceptionDemo.java:45)
     * 	at com.slef.learnjava.exception.ExceptionDemo.process3(ExceptionDemo.java:56)
     * 	... 1 more
     * 	注意到Caused by: Xxx，说明捕获的IllegalArgumentException并不是造成问题的根源，根源在于NullPointerException，是在Main.process2()方法抛出的。
     * 	在代码中获取原始异常可以使用Throwable.getCause()方法。如果返回null，说明已经是“根异常”了。
     * 	有了完整的异常栈的信息，我们才能快速定位并修复代码的问题。
     * 	捕获到异常并再次抛出时，一定要留住原始异常，否则很难定位第一案发现场！
     * @param s
     */
    public static void process3(String s) {
        try {
            process2(s);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 异常屏蔽
     * 如果在执行finally语句时抛出异常，那么，catch语句的异常还能否继续抛出？例如：
     * 这说明finally抛出异常后，原来在catch中准备抛出的异常就“消失”了，因为只能抛出一个异常。没有被抛出的异常称为“被屏蔽”的异常（Suppressed Exception）。
     * 在极少数的情况下，我们需要获知所有的异常。如何保存所有的异常信息？方法是先用origin变量保存原始异常，然后调用Throwable.addSuppressed()，把原始异常添加进来，最后在finally抛出：
     */
    public static void suppressedException() {
        try {
            Integer.parseInt("abc");
        } catch (Exception e) {
            System.out.println("catched");
            throw new RuntimeException(e);
        } finally {
            System.out.println("finally");
            throw new IllegalArgumentException();
        }
    }

    /**
     * 当catch和finally都抛出了异常时，虽然catch的异常被屏蔽了，但是，finally抛出的异常仍然包含了它：
     * 通过Throwable.getSuppressed()可以获取所有的Suppressed Exception。
     * 绝大多数情况下，在finally中不要抛出异常。因此，我们通常不需要关心Suppressed Exception。
     * @throws Exception
     */
    public static void suppressedException01() throws Exception{
        Exception origin = null;
        try {
            System.out.println(Integer.parseInt("abc"));
        } catch (Exception e) {
            origin = e;
            throw e;
        } finally {
            Exception e = new IllegalArgumentException();
            if (origin != null) {
                e.addSuppressed(origin);
            }
            System.out.println("finally");
            throw e;
        }
    }

    /**
     * 对于这种情况，解决办法如下：
     * 方法1（方法testTwo）：直接在新抛出的异常后添加捕获到的异常信息；
     * 方法2（方法testThree）：使用initCause()。
     * https://blog.csdn.net/Thumb_/article/details/120257574
     */
    public static void process4() {
        try {
            process5();
        } catch (IllegalArgumentException e) {
            // 注意 NullPointerException 并没有Throwable 类型的构造方法。。。并不是所有异常都可以找根源
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * 对于这种情况，解决办法如下：
     * 方法1（方法testTwo）：直接在新抛出的异常后添加捕获到的异常信息；
     * 方法2（方法testThree）：使用initCause()。
     * https://blog.csdn.net/Thumb_/article/details/120257574
     */
    public static void process44() {
        try {
            process5();
        } catch (IllegalArgumentException e) {
            // 注意 NullPointerException 并没有Throwable 类型的构造方法。。。并不是所有异常都可以找根源
            NullPointerException exception = new NullPointerException("根据CSDN上面学习的使用initCause()方法，保存原始异常");
            exception.initCause(e);
            throw exception;
        }
    }

    private static void process5() {
        throw new IllegalArgumentException();
    }

    public static void testOne() {
        Integer.parseInt("abc");
    }

    public static void testTwo() throws Exception{
        try {
            testOne();
        } catch (Exception e) {
            throw new Exception("我是转换后的异常01，第一次转换异常");
        }
    }

    public static void testThree() throws Exception{
        try {
            testTwo();
        } catch (Exception e) {
            throw new Exception("我是转换后的异常02,第二次转换异常");
        }
    }

    public static void testStart() {
        try {
            /**
             * java.lang.Exception: 我是转换后的异常02,第二次转换异常
             * 	at com.slef.learnjava.exception.ExceptionDemo.testThree(ExceptionDemo.java:211)
             * 	at com.slef.learnjava.exception.ExceptionDemo.testStart(ExceptionDemo.java:217)
             * 	at com.slef.learnjava.exception.ExceptionDemo.main(ExceptionDemo.java:64)
             * 	可以看到，只输出了最后一个方法的异常信息，丢失了前面两个方法的异常信息，相当于新抛出一个异常会导致前面的异常丢失。
             * 	对于这种情况，解决办法如下：
             * 方法1（方法testTwo）：直接在新抛出的异常后添加捕获到的异常信息；
             * 方法2（方法testThree）：使用initCause()。
             */
            testThree();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void testTwo01() throws Exception{
        try {
            testOne();
        } catch (Exception e) {
            throw new Exception("我是转换后的异常01，第一次转换异常", e);
        }
    }

    public static void testThree02() throws Exception{
        try {
            testTwo01();
        } catch (Exception e) {
            Exception e1 = new Exception("我是转换后的异常02,第二次转换异常");
            e1.initCause(e);
            throw e1;
        }
    }


    /**
     * https://blog.csdn.net/Thumb_/article/details/120257574 《java异常：异常链简介》
     */
    public static void testStart01() {
        try {
            /**
             * java.lang.Exception: 我是转换后的异常02,第二次转换异常
             * 	at com.slef.learnjava.exception.ExceptionDemo.testThree02(ExceptionDemo.java:248)
             * 	at com.slef.learnjava.exception.ExceptionDemo.testStart01(ExceptionDemo.java:267)
             * 	at com.slef.learnjava.exception.ExceptionDemo.main(ExceptionDemo.java:66)
             * Caused by: java.lang.Exception: 我是转换后的异常01，第一次转换异常
             * 	at com.slef.learnjava.exception.ExceptionDemo.testTwo01(ExceptionDemo.java:240)
             * 	at com.slef.learnjava.exception.ExceptionDemo.testThree02(ExceptionDemo.java:246)
             * 	... 2 more
             * Caused by: java.lang.NumberFormatException: For input string: "abc"
             * 	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
             * 	at java.lang.Integer.parseInt(Integer.java:580)
             * 	at java.lang.Integer.parseInt(Integer.java:615)
             * 	at com.slef.learnjava.exception.ExceptionDemo.testOne(ExceptionDemo.java:198)
             * 	at com.slef.learnjava.exception.ExceptionDemo.testTwo01(ExceptionDemo.java:238)
             * 	... 3 more
             *
             * 	可以看到，所有的异常都保留下来拉。
             */
            testThree02();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
