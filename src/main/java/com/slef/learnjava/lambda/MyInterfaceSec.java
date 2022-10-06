package com.slef.learnjava.lambda;

/**
 * 泛型写法
 *
 * 这个接口里只能写一个方法，写多个方法注解会爆红，注解的作用是验证该接口是否是函数接口
 * 若果不写该注解，并且接口只有一个方法则该接口也是函数式接口
 *
 * 作者：小野学Java
 * 链接：https://juejin.cn/post/7148334544755064846
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 * FunctionalInterface
 * 我们把只定义了单方法的接口称之为FunctionalInterface，用注解@FunctionalInterface标记。例如，Callable接口：
 * @FunctionalInterface
 * public interface Callable<V> {
 *     V call() throws Exception;
 * }
 *
 * 再来看Comparator接口：
 * @FunctionalInterface
 * public interface Comparator<T> {
 *
 *     int compare(T o1, T o2);
 *
 *     boolean equals(Object obj);
 *
 *     default Comparator<T> reversed() {
 *         return Collections.reverseOrder(this);
 *     }
 *
 *     default Comparator<T> thenComparing(Comparator<? super T> other) {
 *         ...
 *     }
 *     ...
 * }
 * 虽然Comparator接口有很多方法，但只有一个抽象方法int compare(T o1, T o2)，其他的方法都是default方法或static方法。
 * 另外注意到boolean equals(Object obj)是Object定义的方法，不算在接口方法内。因此，Comparator也是一个FunctionalInterface。
 * 小结
 * 单方法接口被称为FunctionalInterface。
 * 接收FunctionalInterface作为参数的时候，可以把实例化的匿名类改写为Lambda表达式，能大大简化代码。
 * Lambda表达式的参数和返回值均可由编译器自动推断。
 *
 * 使用Lambda表达式，我们就可以不必编写FunctionalInterface接口的实现类，从而简化代码：
 * FunctionalInterface不强制继承关系，不需要方法名称相同，只要求方法参数（类型和数量）与方法返回类型相同，即认为方法签名相同。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1305158055100449
 */
@FunctionalInterface
public interface MyInterfaceSec<T> {

    T method(T t);
}
