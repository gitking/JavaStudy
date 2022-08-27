package com.slef.learnjava.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * https://blog.csdn.net/weixin_49114503/article/details/121651459 《java8 LocalTime的使用方法》
 *
 *
 */
public class LocalTimeDemo {

    public static void main(String[] args) {

        LocalTime min = LocalTime.MIN;
        System.out.println("注意LocalTime的最小时间为:" + min);

        LocalTime MIDNIGHT = LocalTime.MIDNIGHT;
        System.out.println("注意LocalTime的午夜时间为:" + MIDNIGHT);

        LocalTime max = LocalTime.MAX;
        System.out.println("注意LocalTime的最大时间为:" + max);

        // 不要使用JDK的LocalTime.MAX，自己指定一个
        LocalTime localTimeMax = LocalTime.of(23,59,59,0);
        System.out.println("使用自己的localTimeMax:" + localTimeMax);

        /**
         * https://blog.csdn.net/u012410733/article/details/124678331
         * 《Mysql 由于 Java 日期 LocalDateTime 数据精度引发的线上问题》
         * 我们可以查看 Mysql官网(https://dev.mysql.com/doc/refman/5.7/en/fixed-point-types.html)对于 Datetime 这个日期类型的描述。
         * 可以看到 Datetime 这个日期类型的精度为小数点后面 6 位。而我们在查询的时候使用的是 9 位。于是我就使用不同的精度的结束时间进行查询：
         * 所以我们可以得出结论：在使用 Mysql datetime 进行数据查询的时候只能使用正确的精度才能得出符合期望的数据。并且当使用 datetime 大于精度的时候是进行四舍五入的。
         * 建议大家在查询一天内的数据的时候使用 左关右开 模式也就是：
         * 时间 >= ‘2022-05-10 00:00:00’ 并且 时间 < ‘2022-05-11 00:00:00’
         */
        System.out.println("LocalTime.MAX与MySQL的datetime转换的时候有一个坑,LocalTime.MAX的这个值23:59:59.999999999会被MySQL四舍五入之后变成第二天的00:00:00");

        LocalTime str = LocalTime.parse("15:51:01.167526700");
        LocalTime now = LocalTime.now();
        System.out.println("系统当前时间为:" + now);

        LocalTime nowClock = LocalTime.now(Clock.systemUTC());
        System.out.println("获取指定闹钟的时间nowClock:" + nowClock);

        LocalTime zoneLocalTime = LocalTime.now(ZoneId.systemDefault());
        System.out.println("获取指定时区的时间:" + zoneLocalTime);

        LocalTime hm = LocalTime.of(12,12);
        System.out.println("自己指定的时间为:" + hm);

        LocalTime hms = LocalTime.of(12,12,12);
        System.out.println("自己指定的时间为:" + hms);

        LocalTime hmsss = LocalTime.of(12,12,12, 199999999);
        System.out.println("自己指定的时间包含纳秒为:" + hmsss);

        LocalTime sec = LocalTime.ofSecondOfDay(3600);
        System.out.println("根据秒数指定Localtime:" + sec);

        LocalTime sec24Min = LocalTime.ofSecondOfDay(24*60);
        System.out.println("通过一天内的秒创建LocalTime:" + sec24Min);

        LocalTime secSSS = LocalTime.ofNanoOfDay(3600000000000L);
        System.out.println("根据纳秒数指定Localtime:" + secSSS);

        // 一天内最大的纳秒数为:86399999999999,Invalid value for NanoOfDay (valid values 0 - 86399999999999): -1894967296
        LocalTime secMinS = LocalTime.ofNanoOfDay(24*60*1000_000_000L);
        System.out.println("通过一天内的纳秒创建LocalTime:->" + secMinS);

        /**
         * isAfter、isBefore只能比较之前之后，当值相等的时候，需要用compareTo比较
         * 时间比较localTimeA.compareTo(localTimeB)，若相等返回0；若A>B，返回1 ；若A<B返回-1
         */
        System.out.println(secMinS.compareTo(secSSS));

        LocalTime hmsdE = LocalTime.of(12,12,12,99999999);
        System.out.println("获取当前的秒数hmsdE:" + hmsdE.getSecond());
        System.out.println("把LocalTime换算成秒:" + hmsdE.get(ChronoField.SECOND_OF_DAY));
        System.out.println("获取当前的秒数hmsdE:" + hmsdE.get(ChronoField.SECOND_OF_MINUTE));
        System.out.println("获取当前的纳秒数hmsdE:" + hmsdE.get(ChronoField.NANO_OF_SECOND));

        // java.time.temporal.UnsupportedTemporalTypeException: Invalid field 'NanoOfDay' for get() method, use getLong() instead
        // System.out.println("获取当前的秒数hmsdE:" + hmsdE.get(ChronoField.NANO_OF_DAY));
        System.out.println("把LocalTime换算成纳秒hmsdE:" + hmsdE.getLong(ChronoField.NANO_OF_DAY));

        /**
         * https://blog.csdn.net/xiaolong7713/article/details/110909319
         * JDK8获取纳秒的问题
         * JDK8中提供了丰富的时间的工具类，但是实际上JDK8中没有真正获取纳秒的方法，可查看这篇文章，https://stackoverflow.com/questions/1712205/current-time-in-microseconds-in-java,
         * Instant类中可以获取纳秒的方法，本质上是获取的毫秒，后6位都是0。
         * ————————————————
         * 版权声明：本文为CSDN博主「xiaolong7713」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/xiaolong7713/article/details/110909319
         */
        System.out.println("获取纳秒:相当于一天的纳秒数" + LocalTime.now().toNanoOfDay());
        System.out.println("获取从1970到现在经过的纳秒数：" + System.nanoTime());

        Instant instant = Instant.now();
        System.out.println(" Instant类中可以获取纳秒的方法，本质上是获取的毫秒，后6位都是0。获取纳秒:" + instant.getNano());

        /**
         * 查看java.time.Clock类的源码可发现，本质是获取当前的System.currentTimeMillis()生成时间类的。
         * Jdk还有一个方法System.nanoTime()可以提供纳秒的精度测量的方法，一般可以用来计算2个方法之间的差值，不能直接转成时间戳。
         * 可以利用这个特性，初始化t1，获取差值从而获取纳秒。
         * https://github.com/jenetics/jenetics/blob/master/jenetics/src/main/java/io/jenetics/util/NanoClock.java 就是利用这个方法实现的。
         */
        long t1=System.nanoTime();
        long offset=System.currentTimeMillis()*1_000_000 - t1;
        //获取当前的纳秒
        long currentNano=System.nanoTime() + offset;
        System.out.println("获取当前的纳秒" + currentNano);

        /**
         * JDK9之后获取纳秒的问题
         * 之前有提过这方面的缺陷，https://bugs.openjdk.java.net/browse/JDK-8068730，JDK9之后优化了获取纳秒的问题。
         * Instant instant = Instant.now();
         * int tt = instant.getNano();
         * System.out.println(tt);
         * //返回的时间为72765400，已经包含了纳秒。
         * java.time.Clock源码里，增加了VM.getNanoTimeAdjustment方法获取纳秒。
         * 这时候如要获取纳秒的时间戳就简单了。
         * Instant instant = Instant.now();
         * int tt = instant.getNano();                // Represent a moment in UTC.
         * long time = instant.toEpochMilli()/1000*1000_000_000 + tt;
         * System.out.println(time);
         * ————————————————
         * 版权声明：本文为CSDN博主「xiaolong7713」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/xiaolong7713/article/details/110909319
         */

        Date date = new Date();
        System.out.println("java.util.date没有获取当前纳秒的方法");
        System.out.println("获取从1970年到date这个时间的毫秒数:" + date.getTime());

        LocalTime localTime = LocalTime.now();
        System.out.println("LocalTime没有获取毫秒的方法,因为LocalTime是由小时、分钟、秒、纳秒组成的");
        System.out.println("JDK8获取当前时间的纳秒，有BUG，只能获取毫秒，纳秒部分都是0:->" + localTime.getNano());

        LocalDateTime ldt = LocalDateTime.now();
        System.out.println("LocalDateTime没有获取毫秒的方法,因为LocalDateTime是由LocalDate和LocalTime组成的");
        System.out.println("JDK8获取当前时间的纳秒，有BUG，只能获取毫秒，纳秒部分都是0:->" + ldt.getNano());

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

        /**
         * 时间计算，加/减时、分、秒、纳秒…
         *
         */
        LocalTime nowLt = LocalTime.parse("10:51:01.123456789");
        //当前api中unit可以用的有NANOS、MICROS、MILLIS、SECONDS、MINUTES、HOURS、HALF_DAYS。其他单元不可用
        System.out.println("增加1小时：" + nowLt.plus(1, ChronoUnit.HOURS));

        LocalTime nowLtS = LocalTime.parse("23:51:01.123456789");
        System.out.println("增加1小时：" + nowLtS.plusHours(1));

        LocalTime nowLtSs = LocalTime.parse("23:59:01.123456789");
        System.out.println("增加1分钟：" + nowLtSs.plusMinutes(1));

        /**
         * 计算两个时间的间隔
         * 通过Duration计算两个LocalTime相差的时间
         */
        LocalTime start = LocalTime.parse("10:51:01.167526700");
        LocalTime end = LocalTime.parse("15:52:03.167526701");
        //between的用法是end-start的时间，若start的时间大于end的时间，则所有的值是负的
        Duration duration = Duration.between(start, end);
        System.out.println("两个时间相差：" + duration.getSeconds() + "秒，相差：" + duration.toHours() + "小时，相差：" + duration.toMinutes()+"分钟");

        /**
         * ChronoUnit也可以计算两个单元之间的差值。
         * 我们使用ChronoUnit类的between() 方法来执行相同的操作
         * 版权声明：本文为CSDN博主「五月天的尾巴」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/weixin_49114503/article/details/121651459
         */
        LocalTime startChro = LocalTime.parse("10:51:01.167526700");
        LocalTime endChro = LocalTime.parse("15:52:03.167526701");
        long seconds = ChronoUnit.SECONDS.between(startChro , endChro );
        long hour = ChronoUnit.HOURS.between(startChro , endChro );
        long minute = ChronoUnit.MINUTES.between(startChro , endChro );

        System.out.println("我们使用ChronoUnit类的between() 方法来执行相同的操作，两个时间相差："+seconds+"秒，相差："+hour+"小时，相差："+minute+"分钟");

        /**
         * 通过LocalTime类的toSecondOfDay()方法，返回时间对应的秒数，然后计算出两个时间相差的间隔
         * 版权声明：本文为CSDN博主「五月天的尾巴」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/weixin_49114503/article/details/121651459
         */
        LocalTime starttH = LocalTime.parse("10:51:01.167526700");
        LocalTime endtH = LocalTime.parse("15:52:03.167526701");

        int time = endtH.toSecondOfDay() - starttH.toSecondOfDay();
        System.out.println("通过LocalTime类的toSecondOfDay()方法，返回时间对应的秒数，然后计算出两个时间相差的间隔,两个时间相差："+time+"秒");

        // 格式化
        LocalTime time1 = LocalTime.parse("10:51:01.167526700");
        System.out.println(time1); //10:51:01.167526700

        time1 = LocalTime.parse("10:51");
        System.out.println(time1);//10:51

        time1 = LocalTime.parse("10:51:01");
        System.out.println(time1);//10:51:01

        //若使用parse(CharSequence text, DateTimeFormatter formatter)，text格式需与formatter格式一致，否则可能会报错
        time1 = LocalTime.parse("10:51:01", DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(time1);//10:51:01

        String time12 = time1.format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        System.out.println(time12); //10-51-01

        // 修改LocalTime的时、分、秒、纳秒

        LocalTime time3 = LocalTime.parse("10:51:03.167526700");
        //修改LocalTime变量的小时
        System.out.println(time3.withHour(1)); //01:51:03.167526700
        //修改LocalTime变量的分钟
        System.out.println(time3.withMinute(1)); //10:01:03.167526700
        //修改LocalTime变量的秒
        System.out.println(time3.withSecond(1)); //10:51:01.167526700
        //修改LocalTime变量的纳秒
        System.out.println(time3.withNano(1)); //10:51:03.000000001
        //修改LocalTime变量的秒
        System.out.println(time3.with(ChronoField.SECOND_OF_MINUTE, 1)); //10:51:01.167526700

        /**
         * LocalTime的注意事项
         * java.time.LocalTime ->只对时分秒纳秒做出处理
         * 默认格式：HH:mm:ss.SSSSSSSSS 例如：11:23:40.051942200。
         * 当纳秒<=0时，不显示纳秒
         * 当秒<=0且纳秒<=0时，默认只显示时:分 如11：23
         * LocalTime有时候不显示秒？
         * LocalTime有时候会不显示秒，有时候我们需要把时间转化为HH:mm:ss格式时，这可能就是个坑，那么如果让LocalTime输出指定的格式呢？
         */
        LocalTime t2 = LocalTime.parse("10:00:00");
        //不显示秒示例
        System.out.println(t2);//10:00

        //方式一
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(dtf.format(t2)); //10:00:00

        //方式二：
        LocalTime t23= LocalTime.parse("10:00:00", dtf);
        System.out.println(t23.format(DateTimeFormatter.ISO_LOCAL_TIME));//10:00:00
    }
}
