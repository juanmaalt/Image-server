package com.juanmaalt.imageServer.controller

import com.juanmaalt.imageServer.exceptions.BadRequestException
import com.juanmaalt.imageServer.exceptions.InternalServerErrorException
import com.juanmaalt.imageServer.exceptions.ResourceNotFoundException
import com.juanmaalt.imageServer.model.error.ErrorMessage
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandlerController : ResponseEntityExceptionHandler() {
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildResponseEntity(
            ErrorMessage(
                400,
                HttpStatus.BAD_REQUEST,
                "There is a problem with the request. Please check and try again.",
            )
        )
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        return buildResponseEntity(
            ErrorMessage(
                400,
                HttpStatus.BAD_REQUEST,
                "There is a problem with the request. Please check and try again.",
            )
        )
    }

    @ExceptionHandler(value = [BadRequestException::class])
    protected fun badRequestException(ex: BadRequestException, request: WebRequest): ResponseEntity<Any> {
        val message = ErrorMessage(
            400,
            HttpStatus.BAD_REQUEST,
            "There is a problem with the request. Please check and try again.",
        )

        return buildResponseEntity(message)
    }

    @ExceptionHandler(value = [InternalServerErrorException::class])
    protected fun internalServerErrorException(ex: InternalServerErrorException, request: WebRequest): ResponseEntity<Any> {
        val message = ErrorMessage(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Something unexpected happened during the request. Please wait and try again.",
        )

        return buildResponseEntity(message)
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    protected fun resourcerNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val message = ErrorMessage(
            404,
            HttpStatus.NOT_FOUND,
            "The requested resource could not be found but may be available in the future. Please wait and try again.",
        )

        return buildResponseEntity(message)
    }

    private fun buildResponseEntity(errorMessage: ErrorMessage): ResponseEntity<Any> {
        return ResponseEntity(errorMessage, errorMessage.status)
    }
}
