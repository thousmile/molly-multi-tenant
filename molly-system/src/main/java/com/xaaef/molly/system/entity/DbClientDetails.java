package com.xaaef.molly.system.entity;

import com.xaaef.molly.core.tenant.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * <p>
 * 客户端详情
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Entity
@Table(name = "client_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DbClientDetails extends BaseEntity {

    /**
     * 参数主键
     */
    @Id
    private String clientId;

    /**
     * 秘钥
     */
    private String secret;

    /**
     * 客户端名称
     */
    private String name;

    /**
     * 客户端 logo
     */
    private String logo;

    /**
     * 描述
     */
    private String description;

    /**
     * 客户端类型，0.第三方应用  1.内部系统客户端
     */
    private Byte clientType;

    /**
     * 授权类型 数组格式 []
     */
    private String grantTypes;

    /**
     * 域名地址，如果是 授权码模式，\r\n必须校验回调地址是否属于此域名之下
     */
    private String domainName;

    /**
     * 授权作用域
     */
    private String scope;

}
