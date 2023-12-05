package com.xaaef.molly.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 查询类型
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2023/12/5 9:31
 */

@Getter
@ToString
public enum QueryTypeEnum {

    YEAR("year", "年"),

    QUARTER("quarter", "季度"),

    MONTH("month", "月"),

    WEEK("week", "周"),

    DAY("date", "日");

    QueryTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    private final String code;

    private final String description;

    @JsonCreator
    public static QueryTypeEnum create(String value) {
        for (var v : values()) {
            if (v.code.equals(value)) {
                return v;
            }
        }
        return DAY;
    }

}
