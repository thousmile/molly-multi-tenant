package com.xaaef.molly.common.enums;

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
public enum DefaultFlag {

    NO((byte) 0, "不是"),

    YES((byte) 1, "是");

    DefaultFlag(Byte code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    private final Byte code;

    private final String description;

    @JsonCreator
    public static DefaultFlag get(byte value) {
        for (var v : values()) {
            if (v.code == value) {
                return v;
            }
        }
        return NO;
    }

}
