/*
 * Author: rok_root
 * Created time: 2021. 07. 24
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.demo.api.spring.framework.advice.conf;

import kr.co.rokroot.demo.api.spring.framework.advice.BaseConstants;
import kr.co.rokroot.demo.api.spring.framework.advice.intr.AuthorizeInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@EnableWebMvc
@ComponentScan(basePackages = "kr.co.rokroot.demo.api.spring.framework",
        includeFilters = @ComponentScan.Filter({ Controller.class, ControllerAdvice.class }),
        useDefaultFilters = false)
@Import({ SecurityConfig.class, SwaggerConfig.class })
public class DispatcherConfig implements WebMvcConfigurer {

    @Override
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(true)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaTypes(this.getMediaTypes());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(BaseConstants.ROOT, BaseConstants.SWAGGER_UI_URL);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(BaseConstants.SWAGGER_UI_URL)
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler(BaseConstants.SWAGGER_SCRIPTS_URL)
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods(RequestMethod.HEAD.name())
                .allowedMethods(RequestMethod.GET.name())
                .allowedMethods(RequestMethod.POST.name())
                .allowedMethods(RequestMethod.PATCH.name())
                .allowedMethods(RequestMethod.PUT.name())
                .allowedMethods(RequestMethod.DELETE.name())
                .allowedMethods(RequestMethod.OPTIONS.name())
                .allowCredentials(true)
                .maxAge(0L);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setAlwaysUseFullPath(true);
        webContentInterceptor.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePrivate());
        registry.addInterceptor(webContentInterceptor);

        registry.addInterceptor(new AuthorizeInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(this.getExcludePaths());
    }


    protected Map<String, MediaType> getMediaTypes() {
        Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();
        mediaTypes.put("json", MediaType.APPLICATION_JSON);

        return mediaTypes;
    }

    protected List<String> getExcludePaths() {
        List<String> excludePaths = new ArrayList<String>();
        excludePaths.add(BaseConstants.ROOT);
        excludePaths.add(BaseConstants.CSRF);
        excludePaths.add(BaseConstants.RESET);
        excludePaths.add(BaseConstants.FAVICON);
        excludePaths.add(BaseConstants.SWAGGER_UI_URL);
        excludePaths.add(BaseConstants.SWAGGER_API_DOCS);
        excludePaths.add(BaseConstants.SWAGGER_SCRIPTS_URL);
        excludePaths.add(BaseConstants.SWAGGER_RESOURCES_URL);

        return excludePaths;
    }
}
