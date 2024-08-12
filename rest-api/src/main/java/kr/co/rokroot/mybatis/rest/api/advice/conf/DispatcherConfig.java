/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.mybatis.rest.api.advice.conf;

import kr.co.rokroot.mybatis.rest.api.advice.BaseConstants;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@EnableWebMvc
@ComponentScan(basePackages = "kr.co.rokroot.mybatis.rest.apis",
        includeFilters = @ComponentScan.Filter({ Controller.class, ControllerAdvice.class }),
        useDefaultFilters = false)
@Import({ SecurityConfig.class })
public class DispatcherConfig implements WebMvcConfigurer {

    @Override
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        BeanNameViewResolver bean = new BeanNameViewResolver();
        bean.setOrder(1);

        InternalResourceViewResolver view = new InternalResourceViewResolver();
        view.setPrefix("/WEB-INF/");
        view.setSuffix(".jsp");
        view.setOrder(2);

        registry.viewResolver(bean);
        registry.viewResolver(view);
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
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(BaseConstants.ROOT, BaseConstants.SWAGGER_UI_URL);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("/swagger-ui/")
                .setCachePeriod(30000000);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods(RequestMethod.HEAD.name())
                .allowedMethods(RequestMethod.GET.name())
                .allowedMethods(RequestMethod.POST.name())
                .allowedMethods(RequestMethod.PUT.name())
                .allowedMethods(RequestMethod.PATCH.name())
                .allowedMethods(RequestMethod.DELETE.name())
                .allowedMethods(RequestMethod.OPTIONS.name())
                .allowCredentials(true)
                .maxAge(0L);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePrivate());
        registry.addInterceptor(webContentInterceptor);

//        registry.addInterceptor(new AuthorizeInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(this.getExcludePaths());
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
