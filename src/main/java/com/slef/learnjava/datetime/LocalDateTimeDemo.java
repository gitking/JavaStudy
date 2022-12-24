package com.slef.learnjava.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * 廖雪峰
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1303871087444002《LocalDateTime》
 * 从Java 8开始，java.time包提供了新的日期和时间API，主要涉及的类型有：
 *     本地日期和时间：LocalDateTime，LocalDate，LocalTime；
 *     带时区的日期和时间：ZonedDateTime；
 *     时刻：Instant；
 *     时区：ZoneId，ZoneOffset；
 *     时间间隔：Duration。
 * 以及一套新的用于取代SimpleDateFormat的格式化类型DateTimeFormatter。
 * 和旧的API相比，新API严格区分了时刻、本地日期、本地时间和带时区的日期时间，并且，对日期和时间进行运算更加方便。
 * 此外，新API修正了旧API不合理的常量设计：
 *  Month的范围用1~12表示1月到12月；
 *  Week的范围用1~7表示周一到周日。
 *  最后，新API的类型几乎全部是不变类型（和String类似），可以放心使用不必担心被修改。
 *
 *  有的童鞋可能发现Java 8引入的java.timeAPI。怎么和一个开源的Joda Time（https://www.joda.org/）很像？难道JDK也开始抄袭开源了？
 *  其实正是因为开源的Joda Time设计很好，应用广泛，所以JDK团队邀请Joda Time的作者Stephen Colebourne共同设计了java.timeAPI。
 *  Java 8引入了新的日期和时间API，它们是不变类，默认按ISO 8601标准格式化和解析；
 *  使用LocalDateTime可以非常方便地对日期和时间进行加减，或者调整日期和时间，它总是返回新对象；
 *  使用isBefore()和isAfter()可以判断日期和时间的先后；
 *  使用Duration和Period可以表示两个日期和时间的“区间间隔”。
 */
public class LocalDateTimeDemo {

    public static void main(String[] args) {
        // 我们首先来看最常用的LocalDateTime，它表示一个本地日期和时间：
        LocalDate d = LocalDate.now(); // 当前日期
        LocalTime t = LocalTime.now(); // 当前时间
        LocalDateTime dt = LocalDateTime.now(); // 当前日期和时间
        System.out.println("严格按照ISO 8601格式打印" + d);
        System.out.println("严格按照ISO 8601格式打印" + t);
        System.out.println("严格按照ISO 8601格式打印" + dt);
        System.out.println("严格按照ISO 8601格式打印，舍弃毫秒,注意打印出来的没有毫秒" + dt.withNano(0));

        /*
         * 上述代码其实有一个小问题，在获取3个类型的时候，由于执行一行代码总会消耗一点时间，
         * 因此，3个类型的日期和时间很可能对不上（时间的毫秒数基本上不同）。为了保证获取到同一时刻的日期和时间，可以改写如下：
         */
        LocalDateTime dtNew = LocalDateTime.now(); // 当前日期和时间
        LocalDate dNew = dtNew.toLocalDate(); // 转换到当前日期
        LocalTime tNew = dtNew.toLocalTime(); // 转换到当前时间

        // 反过来，通过指定的日期和时间创建LocalDateTime可以通过of()方法：
        LocalDate d2 = LocalDate.of(2019, 11, 30); // 2019-11-30, 注意11=11月
        LocalTime t2 = LocalTime.of(15, 16, 17); // 15:16:17

        LocalDateTime dt3 = LocalDateTime.of(d2, t2);
        LocalDateTime dt2 = LocalDateTime.of(2019, 11, 30, 15, 16, 17);

        // 因为严格按照ISO 8601的格式，因此，将字符串转换为LocalDateTime就可以传入标准格式：
        LocalDateTime dt4 = LocalDateTime.parse("2019-11-19T15:16:17");
        LocalDate d4 = LocalDate.parse("2019-11-19");
        LocalTime t4 = LocalTime.parse("15:16:17");

        /*
         * 注意ISO 8601规定的日期和时间分隔符是T。标准格式如下：
         * 日期：yyyy-MM-dd
         * 时间：HH:mm:ss
         * 带毫秒的时间：HH:mm:ss.SSS
         * 日期和时间：yyyy-MM-dd'T'HH:mm:ss
         * 带毫秒的日期和时间：yyyy-MM-dd'T'HH:mm:ss.SSS
         */

        // 如果要自定义输出的格式，或者要把一个非ISO 8601格式的字符串解析成LocalDateTime，可以使用新的DateTimeFormatter：
        // 自定义格式化
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println("自定义格式化:" + dtf.format(LocalDateTime.now()));

        // 使用自定义格式将字符串解析为LocalDateTime
        LocalDateTime dt5 = LocalDateTime.parse("2019/11/30 15:16:17", dtf);
        System.out.println(dt5);

        // LocalDateTime提供了对日期和时间进行加减的非常简单的链式调用：计算日期

        LocalDateTime dt6 = LocalDateTime.of(2019, 10, 26, 20, 30, 59);
        System.out.println(dt6);

        // 加5天减3小时，注意到月份加减会自动调整日期，例如从2019-10-31减去1个月得到的结果是2019-09-30，因为9月没有31日。
        LocalDateTime dt7 = dt6.plusDays(5).minusHours(3);
        System.out.println("注意月份相加之后会自动调整月份：->" + dt7);

        // 减1月注意减去一个月之后天数也会自动发生变化,因为9月没有31天
        LocalDateTime dt8 = dt7.minusMonths(1);
        System.out.println("注意减去一个月之后天数也会自动发生变化,因为9月没有31天:->" + dt8);

        /*
         * 对日期和时间进行调整则使用withXxx()方法，例如：withHour(15)会把10:11:12变为15:11:12：
         * 调整年：withYear()
         * 调整月：withMonth()
         * 调整日：withDayOfMonth()
         * 调整时：withHour()
         * 调整分：withMinute()
         * 调整秒：withSecond()
         */
        LocalDateTime dt9 = LocalDateTime.of(2019,10,26,20,30,59);
        System.out.println(dt9);
        // 使用with把日期变为31日
        LocalDateTime dt10 = dt9.withDayOfMonth(31);
        System.out.println(dt10);

        // 月份变为9，天数则会自动变为30，9月没有31日
        LocalDateTime dt11 = dt10.withMonth(9);
        System.out.println("把月份变为9月之后，天数会自动变为30日:->" + dt11);

        // 把9月份的天数改完31,此时会报错：java.time.DateTimeException: Invalid date 'SEPTEMBER 31'
        try {
            LocalDateTime dt12 = dt11.withDayOfMonth(31);
            System.out.println("把9月份的天数改为31" + dt12);
        } catch (DateTimeException e) {
            e.printStackTrace();
        }

        /*
         * 实际上，LocalDateTime还有一个通用的with()方法允许我们做更复杂的运算。例如：
         * 对于计算某个月第1个周日这样的问题，新的API可以轻松完成。
         */
        // 本月第一天0:00时刻,注意没有00秒，当秒的部分为00时，LocalDateTime会自动丢弃秒的部分
        LocalDateTime firstDay = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        System.out.println("本月第一天0:00时刻" + firstDay);

        /**
         * 看LocalTimetoString方法的源码：
         * The format used will be the shortest that outputs the full value of the time where the omitted parts are implied to be zero.
         * 使用的格式将是最短的输出时间的完整值，其中省略的部分被暗示为零。
         * HH:mm
         * HH:mm:ss
         * HH:mm:ss.SSS
         * HH:mm:ss.SSSSSS
         * HH:mm:ss.SSSSSSSSS
         * LocalTime是由小时和分钟和秒和纳秒组成的，仔细看LocalTime的toString方法就行了
         */
        System.out.println("使用的格式将是最短的输出时间的完整值，其中省略的部分被暗示为零。" + LocalTime.now().toString());
        System.out.println("使用的格式将是最短的输出时间的完整值，其中省略的部分被暗示为零。" + LocalTime.now().toString());
        String localTimeStr;
//        while (true) {
//            localTimeStr = LocalTime.now().toString();
//            if (localTimeStr.length() > 12) {
//                System.out.println("使用的格式将是最短的输出时间的完整值，其中省略的部分被暗示为零。这里好像获取不到纳秒" + LocalTime.now().toString());
//                break;
//            }
//        }
        // 本月最后一天
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("本月最后一天" + lastDay);

        // 下月第一天
        LocalDate nextMonthFirstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.println("下一个月第一天:" + nextMonthFirstDay);

        // 本月第一个周一： firstInMonth实际取得的是当月的第一个周几的功能，并不能实现获取工作日的功能。你说得有道理，需要循环MONDAY-FRIDAY然后找出最小的一天就是第一个工作日
        LocalDate firstWeekday = LocalDate.now().with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        System.out.println("本月第一个周一：" + firstWeekday);

        // 要判断两个LocalDateTime的先后，可以使用isBefore()、isAfter()方法，对于LocalDate和LocalTime类似：
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = LocalDateTime.of(2019, 11, 19, 8, 15, 0);
        System.out.println("now是否比target的时间要早:" + now.isBefore(target));

        System.out.println(LocalDate.now().isBefore(LocalDate.of(2019,11,19)));
        System.out.println(LocalTime.now().isAfter(LocalTime.parse("08:15:00")));

        LocalDateTime now2 = LocalDateTime.now();
        LocalDateTime target2 = LocalDateTime.now();
        // 那说明你的电脑太快了。
        // LocalDateTime内部持有LocalDate和LocalTime，LocalTime的精度是纳秒，如果能在1ns内跑完这两行代码，拿到的时间就是一样的，否则纳秒不一样
        System.out.println("电脑快不快:如果执行一行代码总会消耗一点时间，那不应该返回true吗？" + now2.isBefore(target2));

        /**
         * 注意到LocalDateTime无法与时间戳进行转换，因为LocalDateTime没有时区，无法确定某一时刻。
         * 后面我们要介绍的ZonedDateTime相当于LocalDateTime加时区的组合，它具有时区，可以与long表示的时间戳进行转换。
         */

        /**
         * Duration和Period,Duration表示两个时刻之间的时间间隔。另一个类似的Period表示两个日期之间的天数：
         * 注意到两个LocalDateTime之间的差值使用Duration表示，类似PT1235H10M30S，表示1235小时10分钟30秒。
         * 而两个LocalDate之间的差值用Period表示，类似P1M21D，表示1个月21天。
         * Duration和Period的表示方法也符合ISO 8601的格式，它以P...T...的形式表示，P...T之间表示日期间隔，T后面表示时间间隔。
         * 如果是PT...的格式表示仅有时间间隔。
         * 利用ofXxx()或者parse()方法也可以直接创建Duration：
         */
        LocalDateTime start = LocalDateTime.of(2019, 11,19,8,15,0);
        LocalDateTime end = LocalDateTime.of(2020, 1, 9,19,25,30);
        Duration dr = Duration.between(start, end);
        System.out.println("Duration表示两个时刻之间的时间间隔" + dr);

        Period p = LocalDate.of(2019, 11, 19).until(LocalDate.of(2020,1,9));
        System.out.println("Period表示两个日期之间的天数" + p);

        Duration dr1 = Duration.ofHours(10); // 10小时
        System.out.println("注意dr1转换成String的时候会自动变成:(PT10H) - >> " + dr1);
        Duration dr2 = Duration.parse("P1DT2H3M");
        System.out.println("注意dr2转换成String的时候会自动变成:(PT26H3M) - >> " + dr2);

        long untilLong = LocalDate.now().until(LocalDate.now().plusDays(11), ChronoUnit.DAYS);
        System.out.println("*******************until直到,LocalDate.now到目标日期需要多少天" + untilLong);
    }
}
