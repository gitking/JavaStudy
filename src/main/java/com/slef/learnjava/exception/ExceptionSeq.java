package com.slef.learnjava.exception;

import java.util.InputMismatchException;

/**
 * https://blog.csdn.net/qq_39696269/article/details/107685866 《e.printStackTrace()和System.out输出顺序混乱》
 *
 * 原因：使用 printStackTrace()的时候默认是输出到System.err中去的，
 * 而普通的输出都是放入System.out，这两者都是对上层封装的输出流，在默认情况下两者是指向Console的文本流。所以两者可能会出现同步问题。
 *
 * 解决1：
 * 在printStackTrace()的时候指定输出流为System.out,通过回避System.err来实现Console中文本流的顺序问题。             ex.printStackTrace(System.out);
 * 解决2：
 * 把printStackTrace()放在catch块中的第一行执行，在打印异常执行，不要执行任何System.out.println代码。
 *
 */
public class ExceptionSeq {

    public static void main(String[] args) {
        /**
         * java.lang.ArithmeticException: / by zero
         * 	at com.slef.learnjava.exception.ExceptionSeq.testSeq(ExceptionSeq.java:21)
         * 	at com.slef.learnjava.exception.ExceptionSeq.main(ExceptionSeq.java:9)
         * ArithmeticException Exception
         * 最后执行输出语句
         * -------------e.printStackTrace()和System.out输出顺序混乱--------------
         *
         * 上面这个输出顺序是严格按照代码的顺序打印的，但是如果你把 testSeq();前面的System.out.println()放开就不行了。
         */
//        System.out.println("-------------e.printStackTrace()和System.out输出顺序混乱--------------");
        testSeq();

        System.out.println("-------------e.printStackTrace()和System.out输出顺序混乱--------------");
//        testSeq01();
    }

    /**
     * e.printStackTrace()和System.out输出顺序混乱
     * https://blog.csdn.net/qq_39696269/article/details/107685866 《e.printStackTrace()和System.out输出顺序混乱》
     */
    public static void testSeq() {
        try {
            int i = 1 / 0;
        } catch (InputMismatchException exception) {
            System.out.println("InputMismatchException Exception");
        } catch (ArithmeticException ex) {
            ex.printStackTrace();
            System.out.println("ArithmeticException Exception");

        }
        System.out.println("最后执行输出语句");
    }

//    public static void testSeq01() {
//        try {
//            int i = 1 / 0;
//        } catch (InputMismatchException exception) {
//            System.out.println("InputMismatchException Exception");
//        } catch (ArithmeticException ex) {
//            ex.printStackTrace();
//            System.out.println("ArithmeticException Exception");
//        }
//        System.out.println("最后执行输出语句");
//    }
}
