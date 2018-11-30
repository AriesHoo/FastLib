package com.flyco.tablayout;

/**
 * @Author: AriesHoo on 2018/11/30 11:28
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public enum TextBold {

    NONE(0),
    SELECT(1),
    BOTH(2);
    private int value;

    TextBold(int value) {
        this.value = value;
    }

    public static TextBold valueOf(int value) {
        switch (value) {
            case 0:
                return NONE;
            case 1:
                return SELECT;
            case 2:
                return BOTH;
            default:
                return NONE;
        }
    }

    public int value() {
        return value;
    }
}
