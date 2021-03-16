package com.juanmaalt.imageServer.config

import com.juanmaalt.imageServer.security.APIKeyFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
class CustomWebSecurityAdapter : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ?.and()
            ?.authorizeRequests()
            ?.antMatchers("/images", "/images/**")?.permitAll()
            ?.anyRequest()?.permitAll()
            ?.and()
            ?.csrf()?.disable()
            ?.headers()?.disable()
            ?.httpBasic()
        http?.addFilterAfter(APIKeyFilter(), BasicAuthenticationFilter::class.java)
    }
}
