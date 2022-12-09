package com.xaaef.molly.core.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 状态 【0.禁用 1.正常 2.锁定 】
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/12 13:53
 */

@Getter
@ToString
public enum UserStatus {

    DISABLE(0, "禁用"),

    LOCKING(2, "锁定"),

    NORMAL(1, "正常");

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static UserStatus get(Integer code) {
        for (var v : values()) {
            if (v.code == code) {
                return v;
            }
        }
        return DISABLE;
    }

    @JsonValue
    private final int code;

    private final String description;

}
