package com.juanmaalt.imageServer.controller

import com.juanmaalt.imageServer.response.HealthResponseBody
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun health(): ResponseEntity<HealthResponseBody> {
        return ResponseEntity(HealthResponseBody("Healthy"), HttpStatus.OK)
    }
}
