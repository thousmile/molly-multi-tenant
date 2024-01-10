package com.xaaef.molly.common.consts;

/**
 * <p>
 * 正则表达式 常量
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.0
 * @date 2024/1/10 9:52
 */

public interface RegexConst {

    // 中国手机号
    String MOBILE = "^1[3-9]\\d{9}$";

    // 电话 + 手机号
    String TEL_PHONE = "^1[3-9]\\d{9}$|^0\\d{2,3}-?\\d{7,8}$";

}
