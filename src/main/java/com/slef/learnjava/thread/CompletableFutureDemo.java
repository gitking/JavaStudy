package com.slef.learnjava.thread;

import com.slef.learnjava.stream.StreamDemo;
import com.slef.learnjava.thread.futurecallable.vo.PriceResultVO;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://heapdump.cn/article/4456384 《JAVA使用CompletableFuture实现流水线并行处理，加速你的接口响应》 架构悟道
 * 在项目开发中，后端服务对外提供API接口一般都会关注响应时长。但是某些情况下，由于业务规划逻辑的原因，我们的接口可能会是一个聚合信息处理类的处理逻辑，比如我们从多个不同的地方获取数据，
 * 然后汇总处理为最终的结果再返回给调用方，这种情况下，往往会导致我们的接口响应特别的慢。
 * 而如果我们想要动手进行优化的时候呢，就会涉及到串行处理改并行处理的问题。在JAVA中并行处理的能力支持已经相对完善，
 * 通过对CompletableFuture的合理利用，可以让我们面对这种聚合类处理的场景会更加的得心应手。
 * 作为JAVA8之后加入的新成员，CompletableFuture的实现与使用上，也处处体现出了函数式异步编程的味道。
 * 一个CompletableFuture对象可以被一个环节接一个环节的处理、也可以对两个或者多个CompletableFuture进行组合处理或者等待结果完成。
 * 通过对CompletableFuture各种方法的合理使用与组合搭配，可以让我们在很多的场景都可以应付自如。
 * Future在应对一些简单且相互独立的异步执行场景很便捷，但是在一些复杂的场景，比如同时需要多个有依赖关系的异步独立处理的时候，或者是一些类似流水线的异步处理场景时，就显得力不从心了。比如：
 * 同时执行多个并行任务，等待最快的一个完成之后就可以继续往后处理
 * 多个异步任务，每个异步任务都需要依赖前一个异步任务执行的结果再去执行下一个异步任务，最后只需要一个最终的结果
 * 等待多个异步任务全部执行完成后触发下一个动作执行
 * 所以呢， 在JAVA8开始引入了全新的CompletableFuture类，它是Future接口的一个实现类。
 * 也就是在Future接口的基础上，额外封装提供了一些执行方法，用来解决Future使用场景中的一些不足，对流水线处理能力提供了支持。
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         *
         * thenCombine: 将两个CompletableFuture对象组合起来进行下一步处理，可以拿到两个执行结果，并传给自己的执行函数进行下一步处理，最后返回一个新的CompletableFuture对象。
         *
         * thenAcceptBoth:与thenCombine类似，区别点在于thenAcceptBoth传入的执行函数没有返回值，即thenAcceptBoth返回值为CompletableFuture<Void>。
         *
         * runAfterBoth:等待两个CompletableFuture都执行完成后再执行某个Runnable对象，再执行下一个的逻辑，类似thenRun。
         *
         * applyToEither:两个CompletableFuture中任意一个完成的时候，继续执行后面给定的新的函数处理。再执行后面给定函数的逻辑，类似thenApply。
         *
         * acceptEither:两个CompletableFuture中任意一个完成的时候，继续执行后面给定的新的函数处理。再执行后面给定函数的逻辑，类似thenAccept。
         *
         * runAfterEither: 等待两个CompletableFuture中任意一个执行完成后再执行某个Runnable对象，可以理解为thenRun的升级版，注意与runAfterBoth对比理解。
         *
         * allOf: 静态方法，阻塞等待所有给定的CompletableFuture执行结束后，返回一个CompletableFuture<Void>结果。
         *
         * anyOf : 静态方法，阻塞等待任意一个给定的CompletableFuture对象执行结束后，返回一个CompletableFuture<Void>结果。
         *
         * 对get和join的方法功能含义说明归纳如下：
         * get(): 等待CompletableFuture执行完成并获取其具体执行结果，可能会抛出异常，需要代码调用的地方手动try...catch进行处理。
         *
         * get(long, TimeUnit):与get()相同，只是允许设定阻塞等待超时时间，如果等待超过设定时间，则会抛出异常终止阻塞等待。
         *
         * join():等待CompletableFuture执行完成并获取其具体执行结果，可能会抛出运行时异常，无需代码调用的地方手动try…catch进行处理。
         */
        // StopWatch创建后立即start启动
        StopWatch stopWatch = StopWatch.createStarted();
        // supplyAsync()方法内部会启动一个线程去异步执行
        CompletableFuture<PriceResultVO> mouBao = CompletableFuture.supplyAsync(()-> new PriceResultVO().getPrice("商品","淘宝"))
                .thenCombine(CompletableFuture.supplyAsync(()-> new PriceResultVO().getDiscounts("商品", "淘宝")), new PriceResultVO()::computeRealPrice);

        CompletableFuture<PriceResultVO> mouDong = CompletableFuture.supplyAsync(()-> new PriceResultVO().getPrice("商品","京东"))
                .thenCombine(CompletableFuture.supplyAsync(()-> new PriceResultVO().getDiscounts("商品", "京东")), new PriceResultVO()::computeRealPrice);

        CompletableFuture<PriceResultVO> douyin = CompletableFuture.supplyAsync(()-> new PriceResultVO().getPrice("商品","抖音"))
                .thenCombine(CompletableFuture.supplyAsync(()-> new PriceResultVO().getDiscounts("商品", "抖音")), new PriceResultVO()::computeRealPrice);

        PriceResultVO minPriceResult = Stream.of(mouBao, mouDong, douyin).map(CompletableFuture::join).sorted(Comparator.comparingInt(PriceResultVO::getRealPrice)).findFirst().get();

        System.out.println("利用Stream获取集合中的最小值，淘宝、京东、抖音三家中最低的价格为:" + minPriceResult.getRealPrice() + ",共耗费:" + stopWatch.getTime() + "毫秒");

        stopWatch.reset();
        stopWatch.start();
        List<PriceResultVO> priceResultVOList = new ArrayList<>();
        priceResultVOList.add(new PriceResultVO("商品","淘宝"));
        priceResultVOList.add(new PriceResultVO("商品","京东"));
        priceResultVOList.add(new PriceResultVO("商品","抖音"));

        /**
         * 注意这里采用了Stream方式来进行遍历与结果的收集、排序与返回。看似正常，但是执行的时候会发现，并没有达到我们预期的效果：
         * 虽然Stream中使用两个map方法，但Stream处理的时候并不会分别遍历两遍，其实写法等同于下面这种写到1个map中处理，改为下面这种写法，其实大家也就更容易明白为啥会没有达到我们预期的整体并行效果了：
         *
         * PriceResultVO result = priceResultVOList.stream().map(priceResultVO -> {
         *            return CompletableFuture.supplyAsync(()->{
         *                         return new PriceResultVO().getPrice(priceResultVO.getProductName(), priceResultVO.getSource());
         *             }).thenCombine(
         *                     CompletableFuture.supplyAsync(() -> { return new PriceResultVO().getDiscounts(priceResultVO.getProductName(), priceResultVO.getSource());}),
         *                    new PriceResultVO()::computeRealPrice).join();
         *         })
         *                 .sorted(Comparator.comparing(PriceResultVO::getRealPrice)).findFirst().get();
         */
        PriceResultVO result = priceResultVOList.stream().map(priceResultVO -> {
           return CompletableFuture.supplyAsync(()->{
                        return new PriceResultVO().getPrice(priceResultVO.getProductName(), priceResultVO.getSource());
            }).thenCombine(
                    CompletableFuture.supplyAsync(() -> { return new PriceResultVO().getDiscounts(priceResultVO.getProductName(), priceResultVO.getSource());}),
                   new PriceResultVO()::computeRealPrice);
        }).map(CompletableFuture::join)
                .sorted(Comparator.comparing(PriceResultVO::getRealPrice)).findFirst().get();

        System.out.println("利用Stream获取集合中的最小值，淘宝、京东、抖音三家中最低的价格为:" + minPriceResult.getRealPrice() + ",共耗费:" + stopWatch.getTime() + "毫秒");

        stopWatch.reset();
        stopWatch.start();
        /**
         * 既然如此，这种场景是不是就不能使用Stream了呢？也不是，其实我们拆开成两个Stream分步操作下其实就可以了。
         * 再看下面的第二种实现代码：
         * 因为Stream的操作具有延迟执行的特点，且只有遇到终止操作（比如collect方法）的时候才会真正的执行。所以遇到这种需要并行处理且需要合并多个并行处理流程的情况下，需要将并行流程与合并逻辑放到两个Stream中，这样分别触发完成各自的处理逻辑，就可以了。
         */
        List<CompletableFuture<PriceResultVO>> priceResultVOList1 = priceResultVOList.stream().map(priceResultVO -> {
            return CompletableFuture.supplyAsync(()->new PriceResultVO().getPrice(priceResultVO.getProductName(), priceResultVO.getSource()))
                    .thenCombine(CompletableFuture.supplyAsync(()->new PriceResultVO().getDiscounts(priceResultVO.getProductName(), priceResultVO.getSource())), new PriceResultVO()::computeRealPrice);
        }).collect(Collectors.toList());

        PriceResultVO priceResultVOSec = priceResultVOList1.stream().map(CompletableFuture::join).sorted(Comparator.comparing(PriceResultVO::getRealPrice)).findFirst().get();

        System.out.println("利用Stream获取集合中的最小值，淘宝、京东、抖音三家中最低的价格为:" + minPriceResult.getRealPrice() + ",共耗费:" + stopWatch.getTime() + "毫秒");


        supplyAsyncDemo("商品", "京东");

        runAsyncDemo();


        /**
         *
         * 具体的方法的描述归纳如下：
         *
         * thenApply:对CompletableFuture的执行后的具体结果进行追加处理，并将当前的CompletableFuture泛型对象更改为处理后新的对象类型，返回当前CompletableFuture对象。
         *
         * thenCompose:与thenApply类似。区别点在于：此方法的入参函数返回一个CompletableFuture类型对象.
         *
         * thenAccept:与thenApply方法类似，区别点在于thenAccept返回void类型，没有具体结果输出，适合无需返回值的场景。
         *
         * thenRun:与thenAccept类似，区别点在于thenAccept可以将前面CompletableFuture执行的实际结果作为入参进行传入并使用，但是thenRun方法没有任何入参，只能执行一个Runnable函数，并且返回void类型。
         *
         * 因为上述thenApply、thenCompose方法的输出仍然都是一个CompletableFuture对象，所以各个方法是可以一环接一环的进行调用，形成流水线式的处理逻辑：
         */
        try {
            // 如果某一个环节执行抛出异常了，会导致整个流水线后续的环节就没法再继续下去了，比如下面的例子：
            CompletableFuture.supplyAsync(()->{ throw new RuntimeException("上一个任务发生异常之后，下一个任务就没办法继续了");}).thenApply(returnVal -> {
                System.out.println("上一个任务发生异常之后，下一个任务就没办法继续了");
                return returnVal;
            }).join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * 那如果我们想要让流水线的每个环节处理失败之后都能让流水线继续往下面环节处理，让后续环节可以拿到前面环节的结果或者是抛出的异常并进行对应的应对处理，就需要用到handle和whenCompletable方法了。
         * whenComplete:与handle类似，区别点在于whenComplete执行后无返回值。
         */
        CompletableFuture.supplyAsync(()->{
            throw new RuntimeException("发生异常之后后面可以继续处理来");
        }).handle((returnObj, e) -> {
            if (e != null) {
                System.out.println("上一个任务发生异常了，导致我拿不到返回值应该怎么处理？" + e.getMessage());
            }
            return returnObj;
        }).join();

    }

    /**
     * supplyAsync:静态方法，用于构建一个CompletableFuture<T>对象，并异步执行传入的函数，允许执行函数有返回值T。
     * supplyAsync或者runAsync创建后便会立即执行，无需手动调用触发。
     */
    public static void supplyAsyncDemo(String product, String source) throws ExecutionException, InterruptedException {
        System.out.println("==============演示CompletableFuture的supplyAsync异步启动一个线程,并获取返回结果================");
        CompletableFuture<Integer> supplyAsyncResult = CompletableFuture.supplyAsync(()-> new PriceResultVO().getPrice(product, source));
        Integer result = supplyAsyncResult.get();
        System.out.println("获取返回结果，你也可以不要返回结果:" + result);
    }

    /**
     * runAsync:静态方法，用于构建一个CompletableFuture<Void>对象，并异步执行传入函数与supplyAsync的区别在于此方法传入的是Callable类型，仅执行，没有返回值。
     * supplyAsync或者runAsync创建后便会立即执行，无需手动调用触发。
     */
    public static void runAsyncDemo () {
        System.out.println("==============演示CompletableFuture的runAsync异步启动一个线程，不能获取返回结果================");
        CompletableFuture<Void> runAsyncDemo = CompletableFuture.runAsync(()-> new PriceResultVO().getPrice("上面","京东"));
    }

    /**
     * 环环相扣处理
     * 在流水线处理场景中，往往都是一个任务环节处理完成后，下一个任务环节接着上一环节处理结果继续处理。
     * CompletableFuture用于这种流水线环节驱动类的方法有很多，相互之间主要是在返回值或者给到下一环节的入参上有些许差异，使用时需要注意区分：
     */
}
