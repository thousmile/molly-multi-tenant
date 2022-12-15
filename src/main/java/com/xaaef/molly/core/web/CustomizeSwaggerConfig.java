package com.xaaef.molly.core.web;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.xaaef.molly.core.auth.jwt.JwtTokenProperties;
import com.xaaef.molly.core.tenant.props.MultiTenantProperties;
import com.xaaef.molly.core.web.props.CustomizeSwaggerProperties;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xaaef.molly.core.tenant.consts.MbpConst.X_PROJECT_ID;
import static com.xaaef.molly.core.tenant.consts.MbpConst.X_TENANT_ID;
import static springfox.documentation.service.ParameterType.HEADER;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.0.1
 * @date 2021/10/21 11:29
 */


@Slf4j
@Configuration
@EnableSwagger2
@AllArgsConstructor
@Import(BeanValidatorPluginsConfiguration.class)
@EnableConfigurationProperties(CustomizeSwaggerProperties.class)
public class CustomizeSwaggerConfig {

    private final CustomizeSwaggerProperties props;

    private final JwtTokenProperties tokenProperties;

    private final ServerProperties sp;


    @Bean(value = "orderApi")
    @Order(value = 1)
    public Docket groupRestApi() {
        String requestURI = "/doc.html";
        if (StringUtils.hasText(sp.getServlet().getContextPath())) {
            requestURI = sp.getServlet().getContextPath() + requestURI;
        }
        String docUrl = UrlUtils.buildFullRequestUrl(
                sp.getSsl() == null ? "http" : "https",
                "localhost",
                sp.getPort(),
                requestURI,
                null
        );
        log.info("Knife4j Swagger2 API Doc {}", docUrl);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo())
                .groupName("REST API")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()));
    }


    private ApiInfo groupApiInfo() {
        return new ApiInfoBuilder()
                .title(props.getTitle())
                .description(props.getDescription())
                .contact(new Contact("王逗逗", "https://xaaef.com", "932560435@qq.com"))
                .termsOfServiceUrl("https://xaaef.com")
                .version(props.getVersion())
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("BearerToken", tokenProperties.getTokenHeader(), "header");
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build();
    }


    List<SecurityReference> defaultAuth() {
        var scopes = new AuthorizationScope[]{
                new AuthorizationScope("global", "accessEverything")
        };
        return CollectionUtils.newArrayList(new SecurityReference("BearerToken", scopes));
    }


    /**
     * 增加如下配置可解决Spring Boot 6.x 与Swagger 3.0.0 不兼容问题
     **/
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier,
                                                                         ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes,
                                                                         CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties,
                                                                         Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }


    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

}
