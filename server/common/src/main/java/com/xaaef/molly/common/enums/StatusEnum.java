package com.xaaef.molly.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * <p>
 * 0.禁用
 * 1.正常
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Getter
@ToString
public enum StatusEnum {

    DISABLE((byte) 0, "禁用"),

    NORMAL((byte) 1, "正常");

    StatusEnum(Byte code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    private final Byte code;

    private final String description;


    /**
     * 用于 spring mvc 实体参数绑定
     */
    @JsonCreator
    public static StatusEnum get(Byte value) {
        for (var v : values()) {
            if (Objects.equals(v.code, value)) {
                return v;
            }
        }
        return NORMAL;
    }

}
