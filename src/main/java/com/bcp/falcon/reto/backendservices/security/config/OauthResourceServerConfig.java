package com.bcp.falcon.reto.backendservices.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@SuppressWarnings("deprecation")
@Configuration
@EnableResourceServer
public class OauthResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests()
                .antMatchers("/h2-console/**")
                .permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and().headers().frameOptions().sameOrigin();

        httpSecurity.authorizeRequests()
                .antMatchers("/v2/api-docs/**", "/swagger-ui.html")
                .permitAll()
                .and()
                .csrf().ignoringAntMatchers("/v2/api-docs/**", "/swagger-ui.html");

        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/oauth/token**")
                .permitAll();

        httpSecurity.authorizeRequests()
                .antMatchers("/api/falcon/services/security-services/v1/authenticate").permitAll()
                .and().authorizeRequests().antMatchers("/api/falcon/**").authenticated();
    }
}
