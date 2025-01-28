package org.example.cinematch.controllers

import org.example.cinematch.exceptions.NotFoundException
import org.example.cinematch.models.User
import org.example.cinematch.models.UserStatus
import org.example.cinematch.repositories.UserRepository
import org.example.cinematch.utils.JwtUtils
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class EmailVerificationController(
    private val userRepository: UserRepository,
    private val jwtUtils: JwtUtils,
) {
    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam("token") token: String,
        model: Model,
    ): String {
        if (!jwtUtils.validateToken(token)) {
            model.addAttribute("errorTitle", "Invalid token.")
            model.addAttribute("message", "Your token is invalid. Please request a new one.")
            return "error"
        }

        if (jwtUtils.isExpired(token)) {
            model.addAttribute("errorTitle", "Expired token.")
            model.addAttribute("message", "Your token is expired. Please request a new one.")
            return "error"
        }

        val userId =
            jwtUtils.decodeVerificationToken(token) ?: run {
                return "error"
            }

        val user: User = userRepository.findById(userId).orElseThrow { NotFoundException("User", userId) }
        return if (user.status == UserStatus.PENDING) {
            user.status = UserStatus.ACTIVE
            userRepository.save(user)
            "verification_success"
        } else {
            model.addAttribute("errorTitle", "Email already verified.")
            model.addAttribute("message", "You can login with your account.")
            "error"
        }
    }
}
