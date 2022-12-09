/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis;

import kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.BaseConstants;
import kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.conf.ApplicationConfig;
import kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.conf.DispatcherConfig;
import org.springframework.core.Ordered;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import jakarta.servlet.*;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

public class ServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        // Context common parameters
        this.registerContextParameter(servletContext);
        // Default character encoding UTF-8
        this.registerCharacterEncodingFilter(servletContext);
        // Setting dispatcher servlet
        this.registerDispatcherServlet(servletContext);
        // Setting openAPI servlet
        this.registerOpenApiServlet(servletContext);
        // Setting application context
        this.registerApplicationContext(servletContext);
    }


    private void registerContextParameter(ServletContext servletContext) {
        servletContext.setInitParameter("webAppRootKey", "kr.co.rokroot.spring.demo.api");
    }

    private void registerCharacterEncodingFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", new CharacterEncodingFilter());
        characterEncodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
    }

    private void registerDispatcherServlet(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(DispatcherConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("web", dispatcherServlet);
        dispatcher.setLoadOnStartup(Ordered.HIGHEST_PRECEDENCE + 1);
        dispatcher.addMapping(BaseConstants.OPEN_WEB);
    }

    private void registerOpenApiServlet(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(DispatcherConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic api = servletContext.addServlet("api", dispatcherServlet);
        api.setLoadOnStartup(Ordered.HIGHEST_PRECEDENCE);
        api.addMapping(BaseConstants.ROOT);
    }

    private void registerApplicationContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));
    }
}