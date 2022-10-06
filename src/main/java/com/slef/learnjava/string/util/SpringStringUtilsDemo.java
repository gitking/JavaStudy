package com.slef.learnjava.string.util;

import org.springframework.util.StringUtils;

/**
 * https://vimsky.com/examples/detail/java-method-org.springframework.util.StringUtils.arrayToDelimitedString.html
 * 《Java StringUtils.arrayToDelimitedString方法代码示例》
 *
 */
public class SpringStringUtilsDemo {
    public static void main(String[] args) {

        String[] arrStr = new String[]{"1", "2", "3"};
        String strRes = StringUtils.arrayToDelimitedString(arrStr, "-");
        System.out.println("将数组中的元素以-分隔:->" + strRes);

        System.out.println("String.join方法更加简单,将数组中的元素以-分隔:->" + String.join("-", arrStr));
    }
}
