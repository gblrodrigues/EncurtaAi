package com.gblrod.encurtaai.exception

import com.gblrod.encurtaai.dto.ErrorResponseDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(LinkNotFoundException::class)
    fun handleLinkNotFound(
        exception: LinkNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        return buildErrorResponse(
            status = HttpStatus.NOT_FOUND,
            message = exception.message ?: "Link not found.",
            path = request.requestURI
        )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        return buildErrorResponse(
            status = HttpStatus.NOT_FOUND,
            message = "The requested endpoint was not found.",
            path = request.requestURI
        )
    }

    @ExceptionHandler(ShortCodeAlreadyExistsException::class)
    fun handleShortCodeAlreadyExists(
        exception: ShortCodeAlreadyExistsException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        return buildErrorResponse(
            status = HttpStatus.CONFLICT,
            message = exception.message ?: "Short code already exists.",
            path = request.requestURI
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        val message = exception.bindingResult
            .fieldErrors
            .firstOrNull()
            ?.defaultMessage
            ?: "Validation failed."

        return buildErrorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = message,
            path = request.requestURI
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponseDto> {
        return buildErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = "An unexpected error occurred.",
            path = request.requestURI
        )
    }

    private fun buildErrorResponse(
        status: HttpStatus,
        message: String,
        path: String
    ): ResponseEntity<ErrorResponseDto> {
        val errorResponseDto = ErrorResponseDto(
            timestamp = OffsetDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            path = path
        )

        return ResponseEntity
            .status(status)
            .body(errorResponseDto)
    }
}