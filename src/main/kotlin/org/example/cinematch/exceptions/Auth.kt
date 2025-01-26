package org.example.cinematch.exceptions

import org.springframework.http.HttpStatus

class UnauthorizedException :
    CustomException(
        message = "Unauthorized access",
        status = HttpStatus.FORBIDDEN,
    )

class InvalidTokenException :
    CustomException(
        message = "Invalid token",
        status = HttpStatus.UNAUTHORIZED,
    )
