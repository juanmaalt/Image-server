package com.juanmaalt.imageServer.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfiguration {

    @Value("\${application.urls.allowed.cors}")
    private val urlsAllowedCors = ""

    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        val arrayOfURLs = urlsAllowedCors.split(",").toTypedArray()

        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins(*arrayOfURLs)
                    .allowedMethods("GET", "POST", "DELETE")
            }
        }
    }
}
