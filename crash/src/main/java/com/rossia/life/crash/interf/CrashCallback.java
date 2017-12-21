package com.rossia.life.crash.interf;

/**
 * @author pd_liu on 2017/12/21.
 *         <p>
 *         异常的Callback。
 *         </p>
 */

public interface CrashCallback {
    /**
     * 异常处理的结果
     * @param errorMsg 信息
     */
    void callback(String errorMsg);
}
