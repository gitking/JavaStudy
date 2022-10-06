package com.slef.learnjava.lambda.entity;

import com.slef.learnjava.lambda.Actor;

/**
 * https://blog.csdn.net/weixin_41126303/article/details/81187002
 * 关于那个第二点：接口方法的第一个参数恰巧是调用引用方法的对象（其引用方法所在类或其父类的实例）
 *
 * 应该是其引用方法所在类或其子类的实例吧。这个其实就是用第一个参数去调用存在于引用方法所在类或其子类的实例方法，
 * 如果引用方法存在于子类，而不再父类中，则会报错。
 *
 */
public class Doctor extends Person{

    public Doctor(){
        super("ss");
    }

    @Override
    public void speak() {
        System.out.println("我是一个医生");
    }

    public static void main(String[] args) {
        // 第一行代码这里会报错
//        Actor actor = Doctor::speak;
        Actor actor1 = Person::speak;
    }
}
