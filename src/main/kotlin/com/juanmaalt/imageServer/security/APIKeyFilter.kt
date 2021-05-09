package com.juanmaalt.imageServer.security

import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class APIKeyFilter(private val key: String) : GenericFilterBean() {

    private val apikey = key

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val castedRequest = request as HttpServletRequest
        val apikeyFromRequest = castedRequest.getHeader("x-authorization")

        when {
            request.method.equals("GET") -> {
                chain?.doFilter(request, response)
            }
            apikey == apikeyFromRequest -> {
                chain?.doFilter(request, response)
            }
            else -> {
                val castedResponse = response as HttpServletResponse
                castedResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to execute this action.")
            }
        }
    }
}
