package com.slef.learnjava.lambda;

@FunctionalInterface
public interface MyInterSec {

    //该接口参数比上述的b方法参数数量多一个，除去第一个，其它类型一致(可兼容，如可以一个int,一个Integer)
    //且Test1::b的Test1是该入参类型Test2的子类(不可颠倒)
    void d(Test1 d, int param1, int param2);
}
