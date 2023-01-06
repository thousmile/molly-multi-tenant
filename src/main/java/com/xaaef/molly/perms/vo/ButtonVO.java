package com.xaaef.molly.perms.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;


/**
 * <p>
 * 用户登录成功后，返回的按钮权限
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 */


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class ButtonVO implements java.io.Serializable {

    /**
     * 标识符
     */
    @Schema(description = "按钮权限 标识符！")
    private String perms;

    /**
     * 标题
     */
    @Schema(description = "按钮权限 标题！")
    private String title;

}
