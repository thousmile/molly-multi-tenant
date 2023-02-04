package com.xaaef.molly.core.web.props;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0.0
 * @date 2020/7/2515:30
 */


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "customize.swagger")
public class CustomizeSwaggerProperties {

    /**
     * 标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;

    /**
     * 版本
     */
    private String version = "1.0.1";

}
