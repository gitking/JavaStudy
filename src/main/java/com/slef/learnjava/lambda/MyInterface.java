package com.slef.learnjava.lambda;

/**
 * 这个接口里只能写一个方法，写多个方法注解会爆红，注解的作用是验证该接口是否是函数接口
 * 若果不写该注解，并且接口只有一个方法则该接口也是函数式接口
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
 *
 * 使用Lambda表达式，我们就可以不必编写FunctionalInterface接口的实现类，从而简化代码：
 * FunctionalInterface不强制继承关系，不需要方法名称相同，只要求方法参数（类型和数量）与方法返回类型相同，即认为方法签名相同。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1305207799545890
 */
@FunctionalInterface
public interface MyInterface {

    void method();
}
