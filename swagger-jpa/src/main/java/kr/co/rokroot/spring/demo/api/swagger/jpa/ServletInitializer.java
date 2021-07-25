/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.jpa;

import kr.co.rokroot.core.exceptions.DemoException;
import kr.co.rokroot.spring.demo.api.swagger.jpa.advice.BaseConstants;
import kr.co.rokroot.spring.demo.api.swagger.jpa.advice.conf.ApplicationConfig;
import kr.co.rokroot.spring.demo.api.swagger.jpa.advice.conf.DispatcherConfig;
import org.springframework.core.Ordered;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

public class ServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Context common parameters
        try {
            this.registerContextParameter(servletContext);
        } catch (Exception e) {
            throw new DemoException("Register context parameter failed", e);
        }
        // Default character encoding UTF-8
        try {
            this.registerCharacterEncodingFilter(servletContext);
        } catch (Exception e) {
            throw new DemoException("Register context loader listener failed", e);
        }
        // Setting dispatcher servlet
        try {
            this.registerDispatcherServlet(servletContext);
        } catch (Exception e) {
            throw new DemoException("Register dispatcher servlet failed", e);
        }
        // Setting application context
        try {
            this.registerApplicationContext(servletContext);
        } catch (Exception e) {
            throw new DemoException("Register character encoding filter failed", e);
        }
        // Setting Log4j file path
        try {

        } catch (Exception e) {
            throw new DemoException("Register Log4j property files failed", e);
        }
    }


    protected void registerContextParameter(ServletContext servletContext) throws Exception {
        servletContext.setInitParameter("webAppRootKey", "kr.co.rokroot.spring.demo.api.jpa");
    }

    protected void registerCharacterEncodingFilter(ServletContext servletContext) throws Exception {
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", new CharacterEncodingFilter());
        characterEncodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
    }

    protected void registerDispatcherServlet(ServletContext servletContext) throws Exception {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(DispatcherConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(Ordered.HIGHEST_PRECEDENCE);
        dispatcher.addMapping(BaseConstants.ROOT);
    }

    protected void registerApplicationContext(ServletContext servletContext) throws Exception {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));
    }
}