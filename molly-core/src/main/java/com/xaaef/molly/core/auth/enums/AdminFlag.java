package com.xaaef.molly.core.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

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

    NO((byte) 0, "非管理员"),

    YES((byte) 1, "是管理员");

    AdminFlag(Byte code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    private final Byte code;

    private final String description;

    @JsonCreator
    public static AdminFlag get(byte value) {
        for (var v : values()) {
            if (v.code == value) {
                return v;
            }
        }
        return NO;
    }

}
