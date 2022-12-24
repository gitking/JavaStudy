package com.slef.learnjava.assertt;

/**
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1264740093521088 《使用断言》 廖雪峰
 *
 * 断言（Assertion）是一种调试程序的方式。在Java中，使用assert关键字来实现断言。 注意是一种调试程序的方式。
 * Java断言的特点是：断言失败时会抛出AssertionError，导致程序结束退出。因此，断言不能用于可恢复的程序错误，只应该用于开发和测试阶段。
 *
 * 这是因为JVM默认关闭断言指令，即遇到assert语句就自动忽略了，不执行。
 * 要执行assert语句，必须给Java虚拟机传递-enableassertions（可简写为-ea）参数启用断言。所以，上述程序必须在命令行下运行才有效果：
 * java -ea Main.java
 * 还可以有选择地对特定地类启用断言，命令行参数是：-ea:com.itranswarp.sample.Main，表示只对com.itranswarp.sample.Main这个类启用断言。
 * 或者对特定地包启用断言，命令行参数是：-ea:com.itranswarp.sample...（注意结尾有3个.），表示对com.itranswarp.sample这个包启动断言。
 * 实际开发中，很少使用断言。更好的方法是编写单元测试，后续我们会讲解JUnit的使用。
 *
 */
public class AssertDemo {

    public static void main(String[] args) {
        double x = Math.abs(-123.45);
        /**
         * 语句assert x >= 0;即为断言，断言条件x >= 0预期为true。如果计算结果为false，则断言失败，抛出AssertionError。
         * 使用assert语句时，还可以添加一个可选的断言消息： assert x >= 0 : "x must >= 0";
         * 这样，断言失败的时候，AssertionError会带上消息x must >= 0，更加便于调试。
         * Java断言的特点是：断言失败时会抛出AssertionError，导致程序结束退出。因此，断言不能用于可恢复的程序错误，只应该用于开发和测试阶段。
         */
        assert x >= 100000;
        assert x >= 0 : "x must >= 0";
        System.out.println(x);
    }
}
