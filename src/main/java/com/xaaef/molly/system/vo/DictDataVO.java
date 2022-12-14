package com.xaaef.molly.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/7/15 10:42
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictDataVO implements java.io.Serializable {

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private Object dictValue;

    /**
     * 字典类型关键字
     */
    @JsonIgnore
    private String typeKey;

    /**
     * 是否默认（1.是 0.否）
     */
    private Boolean isDefault;

}
