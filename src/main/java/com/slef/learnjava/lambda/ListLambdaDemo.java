package com.slef.learnjava.lambda;

import com.slef.learnjava.lambda.entity.UserInfo;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://mp.weixin.qq.com/s/ykHkf0OuuyMsJhy83iVI4w 《聊聊工作中常用的Lambda表达式》 原创 捡田螺的小男孩 捡田螺的小男孩 2022-10-30 08:25 发表于广东
 *
 */
public class ListLambdaDemo {
    public static void main(String[] args) {
        List<UserInfo> userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfo(1L, "捡田螺的小男孩", 18));
        userInfoList.add(new UserInfo(2L, "程序员田螺", 27));
        userInfoList.add(new UserInfo(3L, "捡瓶子的小男孩", 26));

        /*
         * list 转 Map
         * 使用Collectos.toMap的时候,如果有重复会报错, 所以要加(k1, k2) -> k1
         * (k1, k2) - > k1 表示，如果有重复的key，则保留第一个，舍弃第二个
         *
         * 类似的，还有Collectors.toList(), Collectors.toSet(),表示把对应的流转化为List或者Set
         */
//        Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(Random::nextLong, userInfo -> userInfo, (k1, k2) -> k1));

        Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo, (k1, k2) -> k1));
        userInfoMap.keySet().forEach(key-> System.out.println("userId为:" + key.longValue()));

        // JVM编译器可以自动推断出来入参的参数类型必须是UserInfo类型,UserInfo::getUserId()这个实例方法看似没有参数，但实际上有一个隐含的参数this,this的类型就是UserInfo
        Map<Long, UserInfo> userInfoMapSec = userInfoList.stream().collect(Collectors.toMap(ListLambdaDemo::testLambda, userInfo -> userInfo, (k1, k2) -> k1));
        userInfoMapSec.keySet().forEach(key-> System.out.println("没有调用UserInfo的getUserId()，我随机生成的userId为:" + key.longValue()));

        // filter() 过滤，从数组集合中，过滤掉不符合条件的元素，留下符合条件的元素
        // filter过滤，留下超过18岁的用户
        List<UserInfo> userInfoList1 = userInfoList.stream().filter(userInfo -> userInfo.getAge() > 18).collect(Collectors.toList());
        userInfoList1.forEach(a -> System.out.println(a.getUserName()));

        // foreach遍历list,遍历map，真的很丝滑
        List<String> userNameList = Arrays.asList("捡田螺的小男孩", "程序员田螺", "捡瓶子的小男孩");
        userNameList.forEach(System.out::println);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("公众号", "捡田螺的小男孩");
        hashMap.put("职业", "程序员田螺");
        hashMap.put("昵称", "捡瓶子的小男孩");
        // 遍历map的key和value
        hashMap.forEach((k, v) -> System.out.println(k + ":\t" + v));

        // groupingBy分组
        /*
         * 提到分组，相信大家都会想起SQL的group by。我们经常需要一个List做分组操作。比如，按城市分组用户。在java8之前，是这么实现的:
         */
        List<UserInfo> originUserInfoList = new ArrayList<>();
        originUserInfoList.add(new UserInfo(1L, "捡田螺的小男孩", 18, "深圳"));
        originUserInfoList.add(new UserInfo(3L, "捡瓶子的小男孩", 26, "湛江"));
        originUserInfoList.add(new UserInfo(2L, "程序员田螺", 27, "深圳"));
        Map<String, List<UserInfo>> result = new HashMap<>();
        for(UserInfo userInfo : originUserInfoList) {
            String city = userInfo.getCity();
            List<UserInfo> userInfos = result.get(city);
            if (userInfos == null) {
                userInfos = new ArrayList<>();
                result.put(city, userInfos);
            }
            userInfos.add(userInfo);
        }
        // 使用Java8的groupingBy分组器，清爽无比
        Map<String, List<UserInfo>> resultLambda = originUserInfoList.stream().collect(Collectors.groupingBy(UserInfo::getCity));

        // sorted + Compartor 排序,工作中，排序的需求比较多，使用sorted + Comparator排序，真的很香
        List<UserInfo> userInfoList2 = new ArrayList<>();
        userInfoList2.add(new UserInfo(1L, "捡田螺的小男孩", 18));
        userInfoList2.add(new UserInfo(3L, "捡瓶子的小男孩", 26));
        userInfoList2.add(new UserInfo(2L, "程序员田螺", 27));

        userInfoList2 = userInfoList2.stream().sorted(Comparator.comparing(UserInfo::getAge)).collect(Collectors.toList());
        userInfoList2.forEach(a -> System.out.println(a.toString()));

        // 如果想降序排序,则可以使用reversed()反转
        userInfoList2 = userInfoList2.stream().sorted(Comparator.comparing(UserInfo::getAge).reversed()).collect(Collectors.toList());

        // 明天把公司的，按多列排序写到这里 TODO， 还有手机微信上收藏的，按多列分组的写到这里来。

        // distinct 可以去除重复的元素,去重
        List<String> listDistinct = Arrays.asList("A", "B", "F", "A", "C");
        List<String> temp = listDistinct.stream().distinct().collect(Collectors.toList());
        temp.forEach(System.out::println);

        // findFirst返回第一个
        List<String> listFirst = Arrays.asList("A", "B", "F", "A", "C");
        // ifPresent 如果值不为null，就执行目标方法
        listFirst.stream().findFirst().ifPresent(System.out::println);

        // 8. anyMatch是否至少匹配一个元素,检查流是否包含至少一个满足给定谓词的元素
        Stream<String> stream = Stream.of("A", "B", "C", "D");
        boolean match = stream.anyMatch(s -> s.contains("C"));
        System.out.println(match);

        Stream<String> stream1 = Stream.of("A", "B", "C", "D");
        // 9. allMatch 匹配所有元素,检查流是否所有都满足给定谓词的元素
        match = stream1.anyMatch(s -> s.contains("C"));
        System.out.println("流中所有数据是否都满足条件");

        // map 转换，map方法可以帮我们做元素转换,比如一个元素所有字母转化为大写,又或者把获取一个元素对象的某个属性
        List<String> listMap = Arrays.asList("jay", "tianluo");
        // 转换为大写
        List<String> upperCaselist = listMap.stream().map(String::toUpperCase).collect(Collectors.toList());
        upperCaselist.forEach(System.out::println);

        // Reduce，Reduce可以合并流的元素，并生成一个值
        int sum = Stream.of(1, 2, 3, 4).reduce(0, (a, b) -> a + b);
        System.out.println("Reduce可以合并流的元素,并生成一个值:" + sum);

        // peek偷看的意思,peek打印个日志,peek()方法是一个中间Stream操作,有时候我们可以使用peek来打印日志
        List<String> resultPeek = Stream.of("程序员田螺", "捡田螺的小男孩", "捡瓶子的小男孩", "小男孩")
                .filter(a -> a.contains("田螺"))
                .peek(a -> System.out.println("中间偷看一下数据，peek偷偷打印个日志,关注公众号:" + a)).collect(Collectors.toList());

        // Max,Min最大最小,使用lambda流求最大值,最小值,非常方便
        List<UserInfo> userInfoList3 = new ArrayList<>();
        userInfoList3.add(new UserInfo(1L, "捡田螺的小男孩", 18));
        userInfoList3.add(new UserInfo(3L, "捡瓶子的小男孩", 26));
        userInfoList3.add(new UserInfo(2L, "程序员田螺", 27));

        Optional<UserInfo> maxAgeUserInfoOpt = userInfoList3.stream().max(Comparator.comparing(UserInfo::getAge));
        maxAgeUserInfoOpt.ifPresent(userInfo -> System.out.println("max age user:" + userInfo.getAge()));

        Optional<UserInfo> minAgeUserInfoOpt = userInfoList3.stream().min(Comparator.comparing(UserInfo::getAge));
        minAgeUserInfoOpt.ifPresent(userInfo -> System.out.println("min age user:" + userInfo.getAge()));

        // count统计,一般count()表示获取流数据元素总数
        long count = userInfoList3.stream().filter(userInfo -> userInfo.getAge() > 18).count();
        System.out.println("count总数，大于18岁的用户:" + count);

        // 常用函数式接口，其实lambda离不开函数式接口，我们来看下JDK8常用的几个函数式接口:
        /**
         * Function<T, R> 转换型, 接受一个参数，返回一个结果 R apply(T t);
         * Consumer<T> 消费型, 接收一个输入参数，并且无返回操作
         * Predicate<T> 判断型，接收一个输入参数，并且返回布尔结果值
         * Supplier<T> 供给型,无参数，返回结果
         */
        // Function<T, R> 是一个功能转换型的接口，可以将一种类型的数据转化为另外一种类型的数据
        // 获取每个字符串的长度,并且返回
        Function<String, Integer> function = String::length;
        Stream<String> stringStream = Stream.of("程序员田螺", "捡田螺的小男孩", "捡瓶子的小男孩");
        Stream<Integer> integerStream = stringStream.map(function);
        integerStream.forEach(System.out::println);

        // Consumer<T> 是一个消费性接口，通过传入参数，并且无返回的操作
        // 获取每个字符串的长度，并且返回
        Consumer<String> consumer = System.out::println;
        Consumer<String> consume1r = a -> System.out.println(a);
        Consumer<String> consume11r = (a) -> System.out.println(a);
        System.out.println("函数式接口，三种定义方法，都行");
        Stream<String> stringStream1 = Stream.of("程序员田螺", "捡田螺的小男孩", "捡瓶子的小男孩");
        stringStream1.forEach(consumer);
        // stringStream1只能调用一次forEach，调用完这个流就关闭了。
        // Exception in thread "main" java.lang.IllegalStateException: stream has already been operated upon or closed
        try {
            stringStream1.forEach(consume1r);
            stringStream1.forEach(consume11r);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.err.println("stringStream1只能调用一次forEach，调用完这个流就关闭了。");
        }

        // Predicate<T> 是一个判断型接口，并且返回布尔值结果。
        // 获取每个字符串的长度，并且返回
        Predicate<Integer> predicate = a -> a > 18;
        UserInfo userInfo = new UserInfo(2L, "程序员田螺", 27);
        System.out.println("注意看可以直接调用这个函数式接口:" + predicate.test(userInfo.getAge()));

        Predicate<Integer> predicateOther = a -> a > 19;
        predicate.and(predicateOther);
        System.out.println("predicate还可以调用别的方法,默认的方法and,俩个Predicate都满足判断才返回true:" + predicate.test(20));
        System.out.println("predicate还可以调用别的方法,默认的方法and,俩个Predicate都满足判断才返回true:" + predicate.test(10));

        // Supplier<T> 是一个供给型接口, 无参数,有返回值
        Supplier<Integer> supplier = () -> Integer.valueOf("666");
        System.out.println("供给型接口，可以直接调用这个方法" + supplier.get());

        /*
         * 这几个函数在日常开发中，也是可以灵活应用的，比如我们DAO操作完数据库，是会有个result的整型结果返回。我们就可以用Supplier<T>来统一判断是否操作成功。如下：
         *
         *  private void saveDb(Supplier<Integer> supplier) {
                if (supplier.get() > 0) {
                    System.out.println("插入数据库成功");
                }else{
                    System.out.println("插入数据库失败");
                }
            }

            @Test
            public void add() throws Exception {
                Course course=new Course();
                course.setCname("java");
                course.setUserId(100L);
                course.setCstatus("Normal");
                saveDb(() -> courseMapper.insert(course));
            }
         */
    }

    /**
     * JVM编译器可以自动推断出来入参的参数类型必须是UserInfo类型,所以这个静态方法跟UserInfo::getUserId()的方法签名是一样的。
     * JVM编译器可以自动推断出来，这个方法应该怎么去调用。
     * @param userInfo
     * @return
     */
    public static Long testLambda(UserInfo userInfo) {
        return new Random().nextLong();
    }



}
