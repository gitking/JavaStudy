package com.slef.learnjava.thread;

import com.slef.learnjava.thread.futurecallable.vo.PriceResultVO;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/**
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581182447650 《使用CompletableFuture》 廖雪峰
 * https://tech.meituan.com/2022/05/12/principles-and-practices-of-completablefuture.html 《CompletableFuture原理与实践-外卖商家端API的异步化》 美团技术团队
 * 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，这两种方法都不是很好，因为主线程也会被迫等待。
 * 从Java 8开始引入了CompletableFuture，它针对Future做了改进，可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法。
 * 我们以获取股票价格为例，看看如何使用CompletableFuture：
 *
 * 可见CompletableFuture的优点是：
 *   1. 异步任务结束时，会自动回调某个对象的方法；
 *   2. 异步任务出错时，会自动回调某个对象的方法；
 *   3. 主线程设置好回调后，不再关心异步任务的执行。
 * 如果只是实现了异步回调机制，我们还看不出CompletableFuture相比Future的优势。
 * CompletableFuture更强大的功能是，多个CompletableFuture可以串行执行，
 * 例如，定义两个CompletableFuture，第一个CompletableFuture根据证券名称查询证券代码，第二个CompletableFuture根据证券代码查询证券价格，这两个CompletableFuture实现串行操作如下：
 *
 * 最后我们注意CompletableFuture的命名规则：
 *  xxx()：表示该方法将继续在已有的线程中执行；
 *  xxxAsync()：表示将异步在线程池中执行。
 * thenAccept()处理正常结果；
 * exceptional()处理异常结果；
 * thenApplyAsync()用于串行化另一个CompletableFuture；
 * anyOf()和allOf()用于并行化多个CompletableFuture。
 */
public class CompletableFutureDemo2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建异步执行任务,supplyAsync会将任务交给默认的线程池去执行,可以看一下supplyAsync方法的源码，要么使用ForkJoinPool.commonPool()线程池，要么使用new ThreadPerTaskExecutor()线程池
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(CompletableFutureDemo2::fetchPrice);

        // 如果执行成功
        cf.thenAccept((price) -> {
            System.out.println("价格为:" + price);
        });

        // 如果执行失败
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });


        /**
         * 如果只是实现了异步回调机制，我们还看不出CompletableFuture相比Future的优势。
         * CompletableFuture更强大的功能是，多个CompletableFuture可以串行执行，
         * 例如，定义两个CompletableFuture，第一个CompletableFuture根据证券名称查询证券代码，第二个CompletableFuture根据证券代码查询证券价格，这两个CompletableFuture实现串行操作如下：
         */
        System.out.println("======================串行执行任务功能演示==============");
        // 串行功能演示: 先执行一个任务
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(()->{
            return  queryCode("中国石油");
        });

        // cfQuery的上一个任务执行成功之后，注意是成功之后，如果失败了就不会继续执行下一个任务，并把上一个任务的结果当做参数传个下一个任务，注意上一个任务的返回值为String, 当前任务的返回值为Double
        CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync((code) -> {
            // 注意，这里的cfQuery对象上一个ComplletableFuture的对象没有发生变化,我们这里演示的是串行
            // 串行的意思就是一个接一个，上一个任务执行完了，紧接着执行下一个任务，同时串行意味着，下一个任务依赖上一个任务的返回值
            return fetchPriceFil(code);
        });

        // cfFetch串行任务执行成功之后，处理结果
        cfFetch.thenAccept((result)->{
            System.out.println("串行执行任务功能演示: 查到的价格为:" + result);
        });

        /**
         * 除了串行执行外，多个CompletableFuture还可以并行执行。例如，我们考虑这样的场景：
         * 同时从新浪和网易查询证券代码，只要任意一个返回结果，就进行下一步查询价格，查询价格也同时从新浪和网易查询，只要任意一个返回结果，就完成操作：
         * 假如Controller 执行一个 会不会结束掉呢？？？
         */
        System.out.println("======================并行执行任务功能演示==============");

        // 俩个CompletableFuture执行异步查询
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(()->{
            return queryCode("中国石油", "https://finance.sina.com.cn/code/");
        });

        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://money.163.com/code/");
        });

        // 用anyof合并为一个新的CompletableFuture,除了anyOf()可以实现“任意个CompletableFuture只要一个成功”，allOf()可以实现“所有CompletableFuture都必须成功”，这些组合操作可以实现非常复杂的异步流程控制。
        CompletableFuture<Object> cfQueryAny = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 俩个Completable执行异步查询:
        CompletableFuture<Double> cfFetchFromSina = cfQueryAny.thenApplyAsync((code) -> {
            return fetchPriceFil((String)code, "https://finance.sina.com.cn/price");
        });

        CompletableFuture<Double> cfFetchFrom163 = cfQueryAny.thenApplyAsync((code)->{
            return fetchPriceFil((String)code, "https://money.163.com/price/");
        });

        // 用anyof合并会一个新的CompletableFuture
        CompletableFuture<Object> cfFetchSec = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        cfFetchSec.thenAccept((result)->{
            System.out.println("价格为:" + result);
        });




        // 主线程不要立刻关闭，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(10000);
    }

    public static Double fetchPrice() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Math.random() < 0.3) {
            throw  new RuntimeException("随机抛出异常 fetch price failed!");
        }

        return 5 + Math.random() * 20;
    }

    public static String queryCode(String code){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "601857";
    }

    public static String queryCode(String code, String url){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "601857";
    }

    public static Double fetchPriceFil(String code) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        return 5 + Math.random() * 20;
    }

    public static Double fetchPriceFil(String code, String url) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        return 5 + Math.random() * 20;
    }

}
