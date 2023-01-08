package com.subhadev.billshare.userservice.config;

import com.subhadev.billshare.userservice.service.JwtAuthenticationFilter;
import com.subhadev.billshare.userservice.service.JwtAuthenticationManager;
import com.subhadev.billshare.userservice.service.JwtService;
import com.subhadev.billshare.userservice.service.UserDaoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = false, securedEnabled = false, jsr250Enabled = true)
public class JWTSecurityConfig  extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public JWTSecurityConfig(JwtService jwtService, UserDaoService userDaoServiceService) {
        jwtAuthenticationFilter =  new JwtAuthenticationFilter(new JwtAuthenticationManager(jwtService, userDaoServiceService));
    }

    @Override
    protected  void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/users/google/*","/users/email/*").permitAll()
                .antMatchers(HttpMethod.GET,"/users/google/*","/users/email/*").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("PATCH","GET","POST","PUT","DELETE");
    }
}
