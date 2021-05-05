package com.juanmaalt.imageServer.security

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class APIKeyFilter : GenericFilterBean() {

    @Value("\${application.apikey.code}")
    private val apikey: String = "b4b6357e-f5e4-45ec-807b-cf722a710925"

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
