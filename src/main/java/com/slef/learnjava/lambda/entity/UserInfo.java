package com.slef.learnjava.lambda.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserInfo {

    private long userId;

    private String userName;

    private int age;

    private String city;

    private UserInfo() {

    }
    public UserInfo(long userId, String userName, int age) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
    }

    public UserInfo(long userId, String userName, int age, String city) {
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.city = city;
    }

    public long getUserId() {
        System.out.println("找到谁在调用我这个方法了:userName为:" + getUserName() + "age为:" + age);
        System.out.println("Lambda的方法引用是谁在调用这个方法？？？？？ 这个是方法是实例方法啊，必须有对象才能调用啊，UserInfo::getUserId()是怎么调用这个方法的？");
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
