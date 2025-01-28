package org.example.cinematch.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val springTemplateEngine: SpringTemplateEngine,
    @Value("\${app.base.url}") private val baseUrl: String,
) {
    fun sendVerificationEmail(
        to: String,
        verificationToken: String,
    ) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setTo(to)
        helper.setSubject("Cinematch Email Verification")

        val context = Context()
        context.setVariable("verificationUrl", "$baseUrl/verify?token=$verificationToken")

        val emailContent = springTemplateEngine.process("email-verification", context)
        helper.setText(emailContent, true)

        mailSender.send(message)
    }
}
