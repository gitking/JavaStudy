package com.slef.learnjava.lambda.entity;

import lombok.Data;

@Data
public class Person implements Comparable<Person> {
    String name;

    public Person(String name) {
        this.name = name;
    }

    public void speak() {
        System.out.println("我是一个人");
    }

    /**
     * x.compareTo(y) 来“比较x和y的大小”。若返回“负数”，意味着“x比y小”；返回“零”，意味着“x等于y”；返回“正数”，意味着“x大于y”。
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Person o) {
        return this.name.compareTo(o.name);
    }
}
