package com.aries.library.fast.demo.base;

/**
 * @Author: AriesHoo on 2019/7/11 22:00
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class BaseEntity<T> {

    public boolean success;
    public int code;
    public String msg;
    public T data;
}
