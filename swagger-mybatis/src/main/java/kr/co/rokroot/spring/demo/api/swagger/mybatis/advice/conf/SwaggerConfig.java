/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.conf;

import kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.BaseConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

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
                .apis(RequestHandlerSelectors.basePackage("kr.co.rokroot.spring.demo.api.swagger.mybatis.module"))
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
                .useDefaultResponseMessages(false);
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
                        .scopes(Arrays.asList(this.getAuthorizationScopes()))
                        .build()
        );
    }

    protected List<GrantType> getGrantTypes() {
        return Collections.singletonList(
                new ResourceOwnerPasswordCredentialsGrant("/oauth/token")
        );
    }

    protected List<SecurityContext> getSecurityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(this.getSecurityReferences())
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
}
