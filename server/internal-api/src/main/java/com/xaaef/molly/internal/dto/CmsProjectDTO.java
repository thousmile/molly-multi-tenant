package com.xaaef.molly.internal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;


/**
 * <p>
 * 项目表
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */

@Schema(description = "项目")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CmsProjectDTO implements java.io.Serializable {

    /**
     * 项目ID
     */
    @Schema(description = "项目ID")
    private Long projectId;

    /**
     * 项目名称
     */
    @Schema(description = "项目名称")
    private String projectName;

    /**
     * 联系人名称
     */
    @Schema(description = "联系人名称")
    private String linkman;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactNumber;

    /**
     * 联系地址
     */
    @Schema(description = "联系地址")
    private String address;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Long sort;

    /**
     * 项目密码，做一些危险操作时，使用
     */
    @Schema(description = "项目密码，做一些危险操作时，使用")
    private String password;

    /**
     * 状态 【0.禁用 1.正常 2.锁定 】
     */
    @Schema(description = "状态 【0.禁用 1.正常 2.锁定 】")
    private Byte status;

}
