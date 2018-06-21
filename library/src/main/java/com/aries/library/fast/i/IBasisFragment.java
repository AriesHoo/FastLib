package com.aries.library.fast.i;

/**
 * Created: AriesHoo on 2018/6/21 13:38
 * E-Mail: AriesHoo@126.com
 * Function:扩展BasisFragment独有属性
 * Description:
 */
public interface IBasisFragment extends IBasisView {

    /**
     * 控制是否为单个fragment--优化懒加载
     *
     * @return
     */
    boolean isSingle();
}
