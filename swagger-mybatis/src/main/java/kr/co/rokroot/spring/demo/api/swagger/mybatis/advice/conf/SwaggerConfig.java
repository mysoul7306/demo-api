/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.conf;

import com.google.common.collect.Lists;
import kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.BaseConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EnableSwagger2
public class SwaggerConfig {
    protected final String SECURITY_REFERENCE_KEY = "oauth";
    protected final String TOKEN_CHECK_URL = "/oauth/token";

    @Bean
    public SecurityConfiguration securityInfo() {
        return SecurityConfigurationBuilder.builder()
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .scopeSeparator(":")
                .appName("DemoAPI")
                .build();
    }

    @Bean
    public Docket openAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("kr.co.rokroot.demo.api.spring.framework.module"))
                .paths(PathSelectors.any())
                .build()
                .forCodeGeneration(true)
                // Custom uri mapping
                .pathMapping(BaseConstants.ROOT)
                // Custom api info
                .apiInfo(this.getApiInfo())
                // Support media type
                .consumes(this.getConsumeContentTypes())
                .produces(this.getProduceContentTypes())
                // OAuth token authorize
                .securitySchemes(this.getSecuritySchemes())
                .securityContexts(this.getSecurityContexts())
                // Custom response message
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, this.getGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.POST, this.getGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.PUT, this.getGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.PATCH, this.getGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE, this.getGlobalResponseMessages());
    }


    protected ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Demo API")
                .description("Demo API with spring")
                .version("v0.0.1")
                .licenseUrl("https://github.com/mysoul7306/demo-api/")
                .termsOfServiceUrl("https://github.com/mysoul7306/")
                .build();
    }

    protected Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add(MediaType.APPLICATION_JSON_VALUE);
        return consumes;
    }

    protected Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add(MediaType.APPLICATION_JSON_VALUE);
        return produces;
    }

    protected List<SecurityScheme> getSecuritySchemes() {
        return Collections.singletonList(
                new OAuthBuilder().name(this.SECURITY_REFERENCE_KEY)
                        .grantTypes(this.getGrantTypes())
                        .scopes(Lists.newArrayList(this.getAuthorizationScopes()))
                        .build()
        );
    }

    protected List<GrantType> getGrantTypes() {
        return Collections.singletonList(
                new AuthorizationCodeGrantBuilder()
                        .tokenEndpoint(new TokenEndpoint("/oauth/check", "oauth-token"))
                        .tokenRequestEndpoint(new TokenRequestEndpoint("/oauth/token", "token_id", "token_key"))
                        .build()
        );
    }

    protected List<SecurityContext> getSecurityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(this.getSecurityReferences())
                        .forPaths(PathSelectors.any())
                        .build()
        );
    }

    protected List<SecurityReference> getSecurityReferences() {
        return Collections.singletonList(
                SecurityReference.builder()
                        .reference(this.SECURITY_REFERENCE_KEY)
                        .scopes(this.getAuthorizationScopes())
                        .build()
        );
    }

    protected AuthorizationScope[] getAuthorizationScopes() {
        return new AuthorizationScope[] {
                new AuthorizationScope("Read", "Read for demo API"),
                new AuthorizationScope("Write", "Write for demo API")
        };
    }

    protected List<ResponseMessage> getGlobalResponseMessages() {
        return Lists.newArrayList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message("BAD_REQUEST (400)").build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message("UNAUTHORIZED (401)").build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message("FORBIDDEN (403)").build(),
                new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value())
                        .message("NOT_FOUND (404)").build(),
                new ResponseMessageBuilder().code(HttpStatus.NOT_ACCEPTABLE.value())
                        .message("NOT_ACCEPTABLE (406)").build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("INTERNAL_SERVER_ERROR (500)").build(),
                new ResponseMessageBuilder().code(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .message("SERVICE_UNAVAILABLE (503)").build()
        );
    }
}
