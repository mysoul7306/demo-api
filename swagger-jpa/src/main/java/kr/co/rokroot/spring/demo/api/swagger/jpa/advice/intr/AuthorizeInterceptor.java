/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.jpa.advice.intr;

import kr.co.rokroot.core.annotations.Authorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authorize auth = ((HandlerMethod) handler).getMethodAnnotation(Authorize.class);

        log.debug("===================       START       ===================");
        log.debug(" User Device Type : " + request.getHeader("user-agent"));
        log.debug(" Request URI : {}", request.getRequestURI());
        log.debug(" Auth type : {}", auth.type().getDesc());


        log.debug("===================        END        ===================");

        // TODO :: Authorize check
        return true;

//		throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
    }

}
