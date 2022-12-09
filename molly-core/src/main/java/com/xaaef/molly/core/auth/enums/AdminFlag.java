package com.xaaef.molly.core.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 0.租户用户
 * 1. 系统用户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/12 13:53
 */

@Getter
@ToString
public enum AdminFlag {

    NO(0, "非管理员"),

    YES(1, "是管理员");

    AdminFlag(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    private final int code;

    private final String description;

    @JsonCreator
    public static AdminFlag get(Integer value) {
        for (var v : values()) {
            if (v.code == value) {
                return v;
            }
        }
        return NO;
    }

}
