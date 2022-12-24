package com.slef.learnjava.lambda;

/**
 * https://blog.csdn.net/weixin_41126303/article/details/81187002
 * Java 8 In Action之引用特定类型的任意对象的实例方法
 * 看到这你应该就懂了，这个指向任意类型实例方法的方法引用有两个要求：
 *        第一点：接口方法的参数比引用方法的参数多一个
 *        第二点：接口方法的第一个参数恰巧是调用引用方法的对象（其引用方法所在类或其父类的实例）
 *        如有不当之处，望指正。
 * 参考文献：https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
 *                    https://stackoverflow.com/questions/32855138/how-does-a-method-reference-to-an-instance-method-of-an-arbitrary-object-of-a-p
 *                    https://stackoverflow.com/questions/25512532/instance-method-reference-and-lambda-parameters
 *                    https://segmentfault.com/a/1190000012269548
 *                    https://blog.csdn.net/qwe125698420/article/details/53415746
 *                    https://blog.csdn.net/learningcoding/article/details/72539918
 *                    https://blog.idrsolutions.com/2015/02/java-8-method-references-explained-5-minutes/
 * ————————————————
 * 版权声明：本文为CSDN博主「ZengXincs」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/weixin_41126303/article/details/81187002
 *
 */
public class Test1 {

    public void a() {
        System.out.println("调用我吗？");
    }

    public void b(Integer param1, int param2){

    }

    public static void main(String[] args) {
        // 这行代码是什么意思？Test1::a返回的不是void吗？ TODO 方法引用也可以创建对象吗？
        MyInter m = Test1::a;
        m.d(new Test1());

        MyInterSec myInterSec = Test1::b;
        myInterSec.d(new Test2(), 1, 2);

        MyInterThr myInterThr = Test1::b;

        MyInterThr myInterThr1 = (j, k, l) -> j.b(k, l);

       // MyInterface myInterface = Test1::a;
        System.out.println("-----------------");
        MyInterface myInterface1 = new Test1()::a;
        myInterface1.method();

    }
}
