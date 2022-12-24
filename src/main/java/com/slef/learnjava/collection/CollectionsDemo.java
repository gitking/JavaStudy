package com.slef.learnjava.collection;

import java.util.Collections;
import java.util.List;

/**
 * https://mp.weixin.qq.com/s/h_APyU7u9x2JiZrhPVbejw 《吐血推荐17个提升开发效率的“轮子”》  捡田螺的小男孩 2022-10-29 08:08 发表于广东 以下文章来源于苏三说技术 ，作者苏三呀
 *
 */
public class CollectionsDemo {
    public static void main(String[] args) {
        // 1. 把一个字符串传变成List
        List<String> singletonList = Collections.singletonList("把一个字符串变成List");

        // 2. 返回一个空List，不要返回null
        List<String> emptyList = Collections.emptyList();
        List<String> emptyList2 = Collections.EMPTY_LIST;


    }
}
