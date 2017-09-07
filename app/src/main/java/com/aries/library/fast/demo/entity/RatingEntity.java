package com.aries.library.fast.demo.entity;

import java.io.Serializable;

/**
 * Created: AriesHoo on 2017/8/25 16:35
 * Function: 电影评价星级
 * Desc:
 */

public class RatingEntity implements Serializable {
    /**
     * max : 10
     * average : 9.6
     * stars : 50
     * min : 0
     */

    public int max;
    public double average;
    public String stars;
    public int min;
}
