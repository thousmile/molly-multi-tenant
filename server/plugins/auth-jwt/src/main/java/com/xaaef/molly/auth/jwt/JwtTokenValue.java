package com.xaaef.molly.auth.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * token 返回值
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 2.0
 * @date 2019/12/12 23:29
 */


@Schema(description = "jwt token")
@Getter
@Setter
@Builder
public class JwtTokenValue implements java.io.Serializable {

    /**
     * 请求头的KEY。默认 "Authorization"
     */
    @Schema(description = "请求头的KEY")
    private String header;

    /**
     * 表示访问令牌，必选项。
     */
    @Schema(description = "资源令牌")
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 表示令牌类型，该值大小写不敏感，必选项，可以是bearer类型或mac类型。
     */
    @Schema(description = "令牌类型")
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * 表示过期时间，单位为秒。如果省略该参数，必须其他方式设置过期时间。
     */
    @Schema(description = "过期时间，单位为秒")
    @JsonProperty("expires_in")
    private Integer expiresIn;

}
