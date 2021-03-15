package com.juanmaalt.imageServer.model.error

import org.springframework.http.HttpStatus

data class ErrorMessage(
    val statusCode: Int = 400,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
    val message: String
)
