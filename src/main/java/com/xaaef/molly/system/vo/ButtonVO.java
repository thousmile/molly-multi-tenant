package com.xaaef.molly.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <p>
 * 用户登录成功后，返回的按钮权限
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ButtonVO implements java.io.Serializable {

    /**
     * 标识符
     */
    @ApiModelProperty(value = "按钮权限 标识符！")
    private String perms;

    /**
     * 标题
     */
    @ApiModelProperty(value = "按钮权限 标题！")
    private String title;

}
