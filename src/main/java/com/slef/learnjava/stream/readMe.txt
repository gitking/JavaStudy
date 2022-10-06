https://www.liaoxuefeng.com/wiki/1252599548343744/1322402873081889
使用Stream
Java从8开始，不但引入了Lambda表达式，还引入了一个全新的流式API：Stream API。它位于java.util.stream包中。
划重点：这个Stream不同于java.io的InputStream和OutputStream，它代表的是任意Java对象的序列。两者对比如下：
	        java.io	                    java.util.stream
存储	        顺序读写的byte或char	        顺序输出的任意Java对象实例
用途	        序列化至文件或网络	            内存计算／业务逻辑

有同学会问：一个顺序输出的Java对象序列，不就是一个List容器吗？

再次划重点：这个Stream和List也不一样，List存储的每个元素都是已经存储在内存中的某个Java对象，而Stream输出的元素可能并没有预先存储在内存中，而是实时计算出来的。
换句话说，List的用途是操作一组已存在的Java对象，而Stream实现的是惰性计算，两者对比如下：

            java.util.List	            java.util.stream
元素	        已分配并存储在内存	            可能未分配，实时计算
用途	        操作一组已存在的Java对象	        惰性计算

Stream看上去有点不好理解，但我们举个例子就明白了。

如果我们要表示一个全体自然数的集合，显然，用List是不可能写出来的，因为自然数是无限的，内存再大也没法放到List中：

List<BigInteger> list = ??? // 全体自然数?
但是，用Stream可以做到。写法如下：
Stream<BigInteger> naturals = createNaturalStream(); // 全体自然数
我们先不考虑createNaturalStream()这个方法是如何实现的，我们看看如何使用这个Stream。
首先，我们可以对每个自然数做一个平方，这样我们就把这个Stream转换成了另一个Stream：
Stream<BigInteger> naturals = createNaturalStream(); // 全体自然数
Stream<BigInteger> streamNxN = naturals.map(n -> n.multiply(n)); // 全体自然数的平方
因为这个streamNxN也有无限多个元素，要打印它，必须首先把无限多个元素变成有限个元素，
可以用limit()方法截取前100个元素，最后用forEach()处理每个元素，这样，我们就打印出了前100个自然数的平方：
Stream<BigInteger> naturals = createNaturalStream();
naturals.map(n -> n.multiply(n)) // 1, 4, 9, 16, 25...
        .limit(100)
        .forEach(System.out::println);
我们总结一下Stream的特点：它可以“存储”有限个或无限个元素。这里的存储打了个引号，是因为元素有可能已经全部存储在内存中，也有可能是根据需要实时计算出来的。
Stream的另一个特点是，一个Stream可以轻易地转换为另一个Stream，而不是修改原Stream本身。
最后，真正的计算通常发生在最后结果的获取，也就是惰性计算。
Stream<BigInteger> naturals = createNaturalStream(); // 不计算
Stream<BigInteger> s2 = naturals.map(BigInteger::multiply); // 不计算
Stream<BigInteger> s3 = s2.limit(100); // 不计算
s3.forEach(System.out::println); // 计算
惰性计算的特点是：一个Stream转换为另一个Stream时，实际上只存储了转换规则，并没有任何计算发生。
例如，创建一个全体自然数的Stream，不会进行计算，把它转换为上述s2这个Stream，也不会进行计算。再把s2这个无限Stream转换为s3这个有限的Stream，也不会进行计算。只有最后，调用forEach确实需要Stream输出的元素时，才进行计算。我们通常把Stream的操作写成链式操作，代码更简洁：
createNaturalStream()
    .map(BigInteger::multiply)
    .limit(100)
    .forEach(System.out::println);
因此，Stream API的基本用法就是：创建一个Stream，然后做若干次转换，最后调用一个求值方法获取真正计算的结果：
int result = createNaturalStream() // 创建Stream
             .filter(n -> n % 2 == 0) // 任意个转换
             .map(n -> n * n) // 任意个转换
             .limit(100) // 任意个转换
             .sum(); // 最终计算结果






