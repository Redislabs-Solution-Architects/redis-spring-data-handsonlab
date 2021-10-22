package com.redis.lars.exercise5.bootstrap;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/auth-login.html").permitAll()
        .antMatchers("/assets/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/auth-login.html")
        .loginProcessingUrl("/perform_login")
        .defaultSuccessUrl("/index.html")
        .failureUrl("/auth-login.html?error=true");

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception   {
        auth.inMemoryAuthentication()
        .withUser("lars")
        .password("{noop}larsje")
        .roles("USER");
    }

}
