package com.slef.learnjava.thread.futurecallable;

import com.slef.learnjava.thread.futurecallable.vo.PriceResultVO;
import lombok.Data;

import java.util.Random;
import java.util.concurrent.Callable;

@Data
public class CallablePriceResult implements Callable<PriceResultVO> {

    private String productName;

    private String source;

    public CallablePriceResult(String productName, String source) {
        this.productName = productName;
        this.source = source;
    }

    @Override
    public PriceResultVO call() throws Exception {
        System.out.println("正在获取最低价格中");
        PriceResultVO priceResultVO = new PriceResultVO();
        priceResultVO.setRealPrice(priceResultVO.getPrice(productName, source) + priceResultVO.getDiscounts(productName, source));
        return priceResultVO;
    }
}
