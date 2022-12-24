package com.slef.learnjava.optional;

import com.slef.learnjava.optional.entity.Address;
import com.slef.learnjava.optional.entity.City;
import com.slef.learnjava.optional.entity.RopeSkippingValue;
import com.slef.learnjava.optional.entity.UserInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://zhuanlan.zhihu.com/p/164503610 《如何正确的使用Java8中的Optional类来消除代码中的null检查》
 * https://blog.csdn.net/Elliot_Elliot/article/details/120988165 《Java8新特性（案例分析）-- Optional接口妙用：避免嵌套if》
 * https://mp.weixin.qq.com/s?__biz=MzI4Njc5NjM1NQ==&mid=2247490243&idx=1&sn=d372a5b9028e5ca5866ab76f2abf4d61&chksm=ebd625efdca1acf967bf008c79e536034ecb84f318f4ee906b4386e6e80551d9afe43fe75a24&scene=21#wechat_redirect
 * 当我们从之前版本切换到Java 8的时候，不应该还按照之前的思维方式处理null值，Java 8提倡函数式编程，新增的许多API都可以用函数式编程表示，Optional类也是其中之一。
 * 这里有几条关于Optional使用的建议：
 * 尽量避免在程序中直接调用Optional对象的get()和isPresent()方法；
 * 避免使用Optional类型声明实体类的属性；
 * 请记住! Optional不能作为入参的参数!
 * 请不要在getter中滥用Optional.
 *
 * 第一条建议中直接调用get()方法是很危险的做法，如果Optional的值为空，那么毫无疑问会抛出NullPointerException异常，而为了调用get()方法而使用isPresent()方法作为空值检查，
 * 这种做法与传统的用if语句块做空值检查没有任何区别。
 *
 * 第二条建议避免使用Optional作为实体类的属性，它在设计的时候就没有考虑过用来作为类的属性，如果你查看Optional的源代码，你会发现它没有实现java.io.Serializable接口，
 * 这在某些情况下是很重要的（比如你的项目中使用了某些序列化框架），使用了Optional作为实体类的属性，意味着他们不能被序列化。
 */
public class OptionDemo01 {

    /**
     * Optional类
     * java.util.Optional<T>类是一个封装了Optional值的容器对象，Optional值可以为null，如果值存在，调用isPresent()方法返回true，调用get()方法可以获取值。
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 创建Optional对象
         * Optional类提供类三个方法用于实例化一个Optional对象，它们分别为empty()、of()、ofNullable()，这三个方法都是静态方法，可以直接调用。
         */

        // empty()方法用于创建一个没有值的Optional对象：
        // empty()方法创建的对象没有值，如果对emptyOpt变量调用isPresent()方法会返回false，调用get()方法抛出NullPointerException异常。
        // 这样调用方必须通过Optional.isPresent()判断是否有结果。
        Optional<String> emptyOpt = Optional.empty();

        // of()方法的参数不能为null,为null会抛出空指针异常
        Optional<String> notNullOpt = Optional.of("");

        // ofNullable()方法接收一个可以为null的值：如果参数为null，返回的就是Optional.empty();对象
        Optional<String> nullableOpt = Optional.ofNullable(null);

        /**
         * https://blog.csdn.net/Elliot_Elliot/article/details/120988165 《Java8新特性（案例分析）-- Optional接口妙用：避免嵌套if》
         */
        UserInfo userInfo = new UserInfo();
        String cityName = Optional.ofNullable(userInfo)
                .map(UserInfo::getAddress)
                .map(Address::getCity)
                .map(City::getCityName)
                .orElse("返回默认值的城市");
        System.out.println("使用Optional可以写出非常优雅的代码:" + cityName);

        /**
         * 考虑一个场景，需要依据每场跳绳比赛的跳绳次数来进行一些计算，如
         * Map<Integer, Map<Integer, Integer>>
         * Key：跳绳比赛场次id
         * value：当前场次的跳绳次数情况。value的Map中，key为跳绳个数，value为跳绳个数对应的人数。
         * 在微服务架构中，对于Map<Integer, Map<Integer, Integer>>这样的结构，通常是从下游获取的数据，在系统中通常是不易理解的，会增加理解成本。而在DDD领域中，通常需要将它转化为领域内的value object。所以，需要将Map<Integer, Map<Integer, Integer>>转化为易于理解和维护的结构，如Map<Integer, List<RopeSkippingValue>>，其具体含义为
         * Map<Integer, List<RopeSkippingValue>>：每场跳绳比赛中，有多少人的跳绳个数是相同的。
         *
         * 如果不采用Optional和Stream操作，那么从Map<Integer, Map<Integer, Integer>>到Map<Integer, List<RopeSkippingValue>>的转化的代码，将是相对较low的（大家可以尝试下）。对于中大厂而言，不优雅的代码（通常所说的丑代码）是不可接受的。
         * 采用Optional+Stream才做，则可写成以下样式：
         * ————————————————
         * 版权声明：本文为CSDN博主「进击的Coder*」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/Elliot_Elliot/article/details/120988165
         */
        Map<Integer, Map<Integer, Integer>> ropeSkippingInfoMap = new HashMap<Integer, Map<Integer, Integer>>();

        Map<Integer, Integer> integerIntegerMap = new HashMap<>();
        integerIntegerMap.put(2,3);
        ropeSkippingInfoMap.put(1, integerIntegerMap);

        // awesome Optional
        Map<Integer, List<RopeSkippingValue>> integerListMap = (Map<Integer, List<RopeSkippingValue>>) Optional.ofNullable(ropeSkippingInfoMap)
                .orElse(Collections.EMPTY_MAP)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry<Integer, Map<Integer, Integer>>::getKey,
                        ropeSkippingValue -> ropeSkippingValue.getValue()
                                .keySet()
                                .stream()
                                .map(boughtTimes -> new RopeSkippingValue(boughtTimes, ropeSkippingValue.getValue().get(boughtTimes))).collect(Collectors.toList())));

        System.out.println(integerListMap);
    }
}
