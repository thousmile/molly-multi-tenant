package com.xaaef.molly.system.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 菜单的使用人群
 * 0.租户用户
 * 1.系统用户
 * 2.全部用户
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Getter
@ToString
public enum MenuTargetEnum {

    TENANT(0, "租户用户"),

    SYSTEM(1, "系统用户"),

    ALL(2, "菜单");

    MenuTargetEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @EnumValue
    @JsonValue
    private final int code;

    private final String description;

    /**
     * 用于 spring mvc 实体参数绑定
     *
     * @author Wang Chen Chen
     * @date 2021/7/23 10:27
     */
    @JsonCreator
    public static MenuTargetEnum create(Integer value) {
        for (var v : values()) {
            if (v.code == value) {
                return v;
            }
        }
        return ALL;
    }

}
