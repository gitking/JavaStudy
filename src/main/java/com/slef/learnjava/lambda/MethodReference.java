package com.slef.learnjava.lambda;

/**
 * https://blog.csdn.net/weixin_41126303/article/details/81187002 《Java 8 In Action之引用特定类型的任意对象的实例方法》
 * 此种引用类型名称原文为：reference to an instance method of an arbitrary object of a particular type
 *
 * 今天在和同学讨论另外一个问题的时候（直接导致这个问题只有明天再解决了），突然争论到能不能用类来调用实例方法，没想到由此又发现了一个知识盲区。
 * oracle官网文档和Java 8 In Action都没有怎么讲清楚这个问题。
 * 方法引用总共有如下四种类型，这里只介绍第三种，其余三种都较为简单，如有需要，请参见他人博客。
 *      类型                      示例
 * 引用对象的实例方法	    Object::instanceMethodName
 * 引用类的静态方法	    ClassName::staticMethodName
 * 引用类的实例方法	    ClassName::methodName
 * 引用构造方法	        ClassName::new
 * 第三种引用类的实例方法？？？这个名字相当的不准确，在Java 8 In Action是这样介绍的，指向任意类型实例方法的方法引用（我觉得叫类的任意对象的实例方法引用更直观）。
 * 我开始一直想当然的就认为是类::实例方法这样就可以了，结果写了几个发现都用不了，看了官网给出的示例发现又可以，于是，作为当代优秀青年，怎么可能不解决这个问题呢。
 *
 * 方法引用的等效lambda表达式String::compareToIgnoreCase将具有形式参数列表(String a, String b)，其中a和b是用于更好地描述此示例的任意名称。
 * 方法引用将调用该方法a.compareToIgnoreCase(b)。我反正是没看懂这是讲的啥。
 * 之后再查了下，原文是这样的 “ reference to an instance method of an arbitrary object of a particular type ”
 * arbitrary 任意的， particular 特定的，翻译过来就是引用特定类型的任意对象的实例方法。于是乎，知道了类的实例方法调用是有讲究的。那么，有什么样的条件呢。
 *
 * 参考文献：https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
 *                    https://stackoverflow.com/questions/32855138/how-does-a-method-reference-to-an-instance-method-of-an-arbitrary-object-of-a-p
 *                    https://stackoverflow.com/questions/25512532/instance-method-reference-and-lambda-parameters
 *                    https://segmentfault.com/a/1190000012269548
 *                    https://blog.csdn.net/qwe125698420/article/details/53415746
 *                    https://blog.csdn.net/learningcoding/article/details/72539918
 *                    https://blog.idrsolutions.com/2015/02/java-8-method-references-explained-5-minutes/
 *
 * ————————————————
 * 版权声明：本文为CSDN博主「ZengXincs」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/weixin_41126303/article/details/81187002
 */
public class MethodReference {
    public static void main(String[] args) {
        MyCsdnInter m = (j, k, l) -> j.a(k, l);

        //该接口参数比上述的b方法参数数量多一个，除去第一个，其它类型一致(可兼容，如可以一个int,一个Integer)
        //且Test1::b的Test1是该入参类型Test2的子类(不可颠倒)
        MyInterSec ms = Test1::b;
        // 这里很奇怪，MyInterSec 这个函数接口，可以引用Test1父类的方法，但是不能引用Test2子类的方法，真正调用的时候可传new Test2对象
        // 这里跟那个泛型很像
        ms.d(new Test1(), 1 , 2 );
        ms.d(new Test2(), 1 , 2 );
//        MyInterSec ms1 = Test2::b;
        /*
         * 我知道，当MyInterSec类的方法第一个参数为Test1时void d(Test1 d, int param1, int param2);
         * 为啥 MyInterSec ms1 = Test2::b; 会报错了。因为方法签名不一致了
         * Test2::b的方法签名，其实是(Test2 this, Integer param1, int param2)
         * 跟 MyInterSec类的d方法签名不一致。但是真正调用MyInterSec接口的d方法时由于方法签名为： void d(Test1 d, int param1, int param2);
         * 第一个参数传new Test1() 和 new Test2()都可以。
         *
         * 但是如果MyInterSec类的方法第一个参数为Test2时void d(Test2 d, int param1, int param2);
         * MyInterSec ms = Test1::b;方法签名为 (Test1 this, Integer param1, int param2)
         * 和
         * MyInterSec ms = Test2::b;方法签名为 (Test2 this, Integer param1, int param2)
         * 就都可以用了，那是因为，说实话这个没看懂为啥，又可以了。
         * Test1::b;方法签名为 (Test1 this, Integer param1, int param2) 跟 MyInterSec 接口的方法签名完全不一样，怎么可以编译通过呢？
         * 难度是为了保证在真正调用的时候，第一个参数最大化吗？因为如果MyInterSec类的方法第一个参数为Test2时void d(Test2 d, int param1, int param2);
         * 真正调用的时候，第一个参数只能传new Test2() 不能传 new Test1()。
         * 这个明天在知乎和Perfma上面提问吧？
         *
         */
//        MyInterSec ms11 = Test2::c;


    }

    public void a (Integer param1, int param2) {
        /*
         * 该接口参数比上述的a方法参数数量多一个，除去第一个，其它类型一致(可兼容，如可以一个int,一个Integer)
         * 且Test1::a的Test1是该入参类型Test2的子类(不可颠倒)
         */
        System.out.println("该接口参数比上述的a方法参数数量多一个，除去第一个，其它类型一致(可兼容，如可以一个int,一个Integer),且Test1::a的Test1是该入参类型Test2的子类(不可颠倒) ");
    }
}

