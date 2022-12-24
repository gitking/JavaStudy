package com.slef.learnjava.exception;

import java.util.InputMismatchException;

public class ExceptionSeq2 {

    public static void main(String[] args) {
        System.out.println("-------------e.printStackTrace()和System.out输出顺序混乱--------------");
        testSeq();

        System.out.println("-------------e.printStackTrace()和System.out输出顺序混乱--------------");
        testSeq01();
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
            System.out.println("ArithmeticException Exception");
            ex.printStackTrace(System.out);

        }
        System.out.println("最后执行输出语句");
    }

    public static void testSeq01() {
        try {
            int i = 1 / 0;
        } catch (InputMismatchException exception) {
            System.out.println("InputMismatchException Exception");
        } catch (ArithmeticException ex) {
            ex.printStackTrace(System.out);
            System.out.println("ArithmeticException Exception");
        }
        System.out.println("最后执行输出语句");
    }
}
