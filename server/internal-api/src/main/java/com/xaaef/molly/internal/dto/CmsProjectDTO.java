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
     * 领导名称
     */
    @Schema(description = "领导名称")
    private String leader;

    /**
     * 领导手机号
     */
    @Schema(description = "领导手机号")
    private String leaderMobile;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Long sort;

    /**
     * 所在地，如：广东省/深圳市/龙岗区-左右云创谷
     */
    @Schema(description = "所在地，如：广东省/深圳市/龙岗区-左右云创谷")
    private String address;

    /**
     * 经度
     */
    @Schema(description = "经度")
    private Double lng;

    /**
     * 纬度
     */
    @Schema(description = "纬度")
    private Double lat;

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
