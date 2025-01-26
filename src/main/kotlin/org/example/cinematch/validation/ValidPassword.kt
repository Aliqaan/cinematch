package org.example.cinematch.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordStrengthValidator::class])
annotation class ValidPassword(
    val message: String = "Password does not meet strength requirements",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)

class PasswordStrengthValidator : ConstraintValidator<ValidPassword, String> {
    override fun isValid(
        password: String?,
        context: ConstraintValidatorContext,
    ): Boolean {
        if (password == null) return false

        if (password.length < 8) {
            context.disableDefaultConstraintViolation()
            context
                .buildConstraintViolationWithTemplate("Password must be at least 8 characters long")
                .addConstraintViolation()
            return false
        }

        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&_-])[A-Za-z\\d@\$!%*?&_-]{8,}\$")
        if (!password.matches(passwordRegex)) {
            context.disableDefaultConstraintViolation()
            context
                .buildConstraintViolationWithTemplate(
                    "Password must contain at least one uppercase letter, lowercase letter, digit, and special character (@\$!%*?&_-)",
                ).addConstraintViolation()
            return false
        }
        return true
    }
}
