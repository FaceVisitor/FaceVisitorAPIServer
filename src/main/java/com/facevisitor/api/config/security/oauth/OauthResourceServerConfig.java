package com.facevisitor.api.config.security.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@EnableResourceServer
@Configuration
public class OauthResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String SERVER_RESOURCE_ID = "face_visitor_oauth2";



    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(SERVER_RESOURCE_ID);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/api/v1/").permitAll()
//                .antMatchers("auth/**").permitAll()
//                .antMatchers("/api/v1/auth/**").permitAll()
//                .antMatchers("/api/v1/owner/**").hasAnyRole("OWNER","SUPER")
//                .antMatchers("/api/v1/owner").permitAll()
//                .antMatchers("/api/v1/owner/auth/**").permitAll()
//                .antMatchers("/api/v1/owner/auth/**").permitAll()
//                .antMatchers("/api/v1/user/exist").permitAll()
                .anyRequest().permitAll()
                .and().csrf().disable()
                .formLogin().disable();
    }
}
