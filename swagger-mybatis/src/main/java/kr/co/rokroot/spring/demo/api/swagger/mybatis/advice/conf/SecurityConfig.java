/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice.conf;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO :: Security
        http.csrf().disable();
//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//				.requireCsrfProtectionMatcher(new RequestHeaderRequestMatcher("*"))
//				.and().exceptionHandling().accessDeniedHandler()
//				.configure(http);
    }
}
