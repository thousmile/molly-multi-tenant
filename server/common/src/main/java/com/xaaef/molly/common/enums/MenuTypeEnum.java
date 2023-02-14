package com.xaaef.molly.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 菜单的类型
 * 0.菜单
 * 1.按钮
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Getter
@ToString
public enum MenuTypeEnum {

    MENU(0, "菜单"),

    BUTTON(1, "按钮");

    MenuTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

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
    public static MenuTypeEnum create(Integer value) {
        for (var v : values()) {
            if (v.code == value) {
                return v;
            }
        }
        return MENU;
    }

}
