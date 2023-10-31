package com.xaaef.molly.internal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作用户
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2023/10/31 13:39
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OperateUserDTO implements java.io.Serializable {

    /**
     * 用户唯一id
     */
    @Schema(description = "用户唯一id")
    private Long userId;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

}
