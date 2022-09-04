package com.slef.learnjava.thread.futurecallable.vo;

import lombok.Data;

import java.util.Random;

@Data
public class PriceResultVO {

    private String productName;

    private String source;

    private Integer realPrice;

    public PriceResultVO(String productName, String source) {
        this.productName = productName;
        this.source = source;
    }

    public PriceResultVO() {

    }

    /**
     * 根据商品去获取指定source数据源上面的最低价格
     *
     * @param price 商品价格
     * @param discounts 商品优惠
     * @return 商品价格信息
     */
    public PriceResultVO computeRealPrice(Integer price, Integer discounts) {
        PriceResultVO pr = new PriceResultVO();
        pr.setRealPrice(price + discounts);
        return pr;
    }

    /**
     * 根据商品去获取指定source数据源上面的最低价格
     *
     * @param productName 商品名称
     * @param source 数据源
     * @return 商品价格信息
     */
    public Integer getPrice(String productName, String source) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("当前线程名字为:" + Thread.currentThread().getName());
        return new Random().nextInt();
    }

    /**
     * 根据商品去获取指定source数据源上面的最低折扣
     *
     * @param productName 商品名称
     * @param source 数据源
     * @return 折扣
     */
    public Integer getDiscounts(String productName, String source) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("当前线程名字为:" + Thread.currentThread().getName());
        return new Random().nextInt();
    }
}
