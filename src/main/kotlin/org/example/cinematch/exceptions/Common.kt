package org.example.cinematch.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(
    resourceName: String,
    resourceId: Any,
) : CustomException(
        message = "$resourceName with id $resourceId is not found",
        status = HttpStatus.NOT_FOUND,
    )

class AlreadyExistsException(
    resourceName: String,
    resourceId: Any,
) : CustomException(
        message = "$resourceName with id $resourceId already exists",
        status = HttpStatus.CONFLICT,
    )

class BadRequestException(
    message: String,
) : CustomException(
        message = message,
        status = HttpStatus.BAD_REQUEST,
    )
