package com.slef.learnjava.string;

/**
 * https://heapdump.cn/article/181573?from=pc 《字符串字面量长度是有限制的》 since1986
 * 只是字面量长度是有限的
 */
public class StringDemo {

    public static void main(String[] args) {
        String str = "";
        while (str.length() < 1000000) {
            str =  str + "1";
        }
        System.out.println("字符串的长度为100万:" + str.length());
    }
}
