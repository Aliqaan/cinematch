package org.example.cinematch.exceptions

import org.springframework.http.HttpStatus

open class CustomException(
    override val message: String,
    val status: HttpStatus,
) : RuntimeException(message)
