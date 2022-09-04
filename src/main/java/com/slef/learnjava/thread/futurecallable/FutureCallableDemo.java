package com.slef.learnjava.thread.futurecallable;

import com.slef.learnjava.thread.futurecallable.vo.PriceResultVO;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * https://heapdump.cn/article/4456384 《JAVA使用CompletableFuture实现流水线并行处理，加速你的接口响应》 架构悟道
 *
 * Future在应对并行结果组合以及后续处理等方面显得力不从心，弊端明显：
 * Future代码写起来会非常拖沓：先封装Callable函数放到线程池中去执行查询操作，然后分三组阻塞等待结果并计算出各自结果，最后再阻塞等待价格计算完成后汇总得到最终结果。
 * 说到这里呢，就需要我们新的主人公CompletableFuture登场了，通过它我们可以很轻松的来完成任务的并行处理，以及各个并行任务结果之间的组合再处理等操作。我们使用CompletableFuture编写实现代码如下
 */
public class FutureCallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // StopWatch创建后立即start启动
        StopWatch stopWatch = StopWatch.createStarted();

        FutureTask<PriceResultVO> mouBaoFuture = new FutureTask<>(new CallablePriceResult("商品名字", "京东"));
        FutureTask<PriceResultVO> jdFuture = new FutureTask<>(new CallablePriceResult("商品名字", "淘宝"));
        FutureTask<PriceResultVO> douYinFuture = new FutureTask<>(new CallablePriceResult("商品名字", "抖音"));
        new Thread(mouBaoFuture).start();
        new Thread(jdFuture).start();
        new Thread(douYinFuture).start();

//        PriceResultVO moubaoPriceResult = mouBaoFuture.get();
//        System.out.println("mouBaoFuture.get()方法会在这里一直阻塞，直到获取到返回值。某宝的最低价钱为:" + moubaoPriceResult.getRealPrice());
//
//        PriceResultVO jdPriceResult = jdFuture.get();
//        System.out.println("jdFuture.get()方法会在这里一直阻塞，直到获取到返回值。某宝的最低价钱为:" + jdPriceResult.getRealPrice());
//
//        PriceResultVO douYinPriceResult = douYinFuture.get();
//        System.out.println("douYinFuture.get()方法会在这里一直阻塞，直到获取到返回值。某宝的最低价钱为:" + douYinPriceResult.getRealPrice());

        PriceResultVO minPrice = Stream.of(mouBaoFuture, jdFuture, douYinFuture).map(priceResultVOFutureTask -> {
            try {
                return priceResultVOFutureTask.get(5L, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.out.println("获取最低价格时发生超时");
                return null;
            }
        }).filter(Objects::nonNull).min(Comparator.comparing(PriceResultVO::getRealPrice)).get();

        stopWatch.stop();
        System.out.println("利用Stream获取集合中的最小值，淘宝、京东、抖音三家中最低的价格为:" + minPrice.getRealPrice() + ",共耗费:" + stopWatch.getTime() + "毫秒");
    }
}
