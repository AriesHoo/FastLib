package com.aries.library.fast.demo;

import com.aries.library.fast.util.SPUtil;

public class AppData {

    public static float saturation = 1.0f;


    public static float getSaturation() {
        float sat = (float) SPUtil.get(App.getContext(), "saturation", 1.0f);
        return sat;
    }

    public static boolean setSaturation(float sat) {
        return SPUtil.put(App.getContext(), "saturation", sat);
    }
}
