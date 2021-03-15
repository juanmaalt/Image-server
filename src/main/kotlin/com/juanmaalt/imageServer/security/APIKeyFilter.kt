/*
package com.juanmaalt.imageServer.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class APIKeyFilter : GenericFilterBean() {

    @Value("\${application.key}")
    lateinit var apikey: String

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val castedRequest = request as HttpServletRequest
        val apikeyFromRequest = castedRequest.getHeader("x-authorization")

        if(apikey == apikeyFromRequest){
            chain?.doFilter(request, response)
        }
    }
}*/
