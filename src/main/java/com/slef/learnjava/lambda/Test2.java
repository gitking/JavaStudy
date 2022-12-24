package com.slef.learnjava.lambda;

/**
 * https://blog.csdn.net/weixin_41126303/article/details/81187002
 * Java 8 In Action之引用特定类型的任意对象的实例方法
 *
 */
public class Test2 extends Test1 {

    public void c(Integer param1, int param2) {
    }


    public void testS(Test1 test1) {
        System.out.println("参数是父类Test1，传子类Test2也一样可以");
    }

    public void testS1(Test2 test1) {
        System.out.println("参数是子类Test2，传父类Test1是不行的");
    }

    public static void main(String[] args) {
        Test2 test2 = new Test2();
        test2.testS(test2);
    }
}
