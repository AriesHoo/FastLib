package com.aries.library.fast.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

/**
 * Created: AriesHoo on 2018/6/30/030 18:21
 * E-Mail: AriesHoo@126.com
 * Function:SharedPreferences工具类
 * Description:
 */
public class SPUtil {

    public static boolean put(Context context, String key, Object object) {
        if (context == null) {
            return false;
        }
        return put(context, context.getPackageName(), key, object);
    }

    /**
     * 存放object
     *
     * @param context
     * @param fileName
     * @param key
     * @param object
     * @return
     */
    public static boolean put(Context context, String fileName, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, ((Integer) object).intValue());
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) object).booleanValue());
        } else if (object instanceof Float) {
            editor.putFloat(key, ((Float) object).floatValue());
        } else if (object instanceof Long) {
            editor.putLong(key, ((Long) object).longValue());
        } else if (object instanceof Set) {
            editor.putStringSet(key, (Set<String>) object);
        } else {
            editor.putStringSet(key, (Set<String>) object);
        }
        return editor.commit();
    }

    public static Object get(Context context, String key, Object def) {
        if (context == null) {
            return def;
        }
        return get(context, context.getPackageName(), key, def);
    }

    /**
     * 获取存放object
     *
     * @param context
     * @param fileName
     * @param key
     * @param def
     * @return
     */
    public static Object get(Context context, String fileName, String key, Object def) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (def instanceof String) {
            return sp.getString(key, def.toString());
        } else if (def instanceof Integer) {
            return sp.getInt(key, ((Integer) def).intValue());
        } else if (def instanceof Boolean) {
            return sp.getBoolean(key, ((Boolean) def).booleanValue());
        } else if (def instanceof Float) {
            return sp.getFloat(key, ((Float) def).floatValue());
        } else if (def instanceof Long) {
            return sp.getLong(key, ((Long) def).longValue());
        } else if (def instanceof Set) {
            return sp.getStringSet(key, (Set<String>) def);
        }
        return def;
    }

    public static boolean remove(Context context, String key) {
        if (context == null) {
            return false;
        }
        return remove(context, context.getPackageName(), key);
    }

    public static boolean remove(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean clearAll(Context context) {
        if (context == null) {
            return false;
        }
        return clearAll(context, context.getPackageName());
    }

    public static boolean clearAll(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }

    public static boolean contains(Context context, String key) {
        if (context == null) {
            return false;
        }
        return contains(context, context.getPackageName(), key);
    }

    public static boolean contains(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public static Map<String, ?> getAll(Context context) {
        if (context == null) {
            return null;
        }
        return getAll(context, context.getPackageName());
    }

    public static Map<String, ?> getAll(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getAll();
    }
}
