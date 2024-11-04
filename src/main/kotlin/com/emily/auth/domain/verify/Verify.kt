package com.emily.auth.domain.verify

import com.emily.auth.presentation.auth.AuthRequest

const val MINIMAL_USERNAME_LENGTH = 3
const val MINIMAL_PASSWORD_LENGTH = 7

class Verify {
    companion object {
        fun verifySignUp(request: AuthRequest): VerifyResult {

            // initial check to see if all fields aren't blank
            if (request.username.isBlank() || request.password.isBlank()) {
                return VerifyResult(
                    success = false,
                    message = "all fields must be filled"
                )
            }

            val messages = mutableListOf<String>()

            // username checks
            if (!request.username.all { it.isLetterOrDigit() || it == '.' || it == '_' }) {
                messages.add("username cannot contain special characters")
            }
            if (request.username.length < MINIMAL_USERNAME_LENGTH) {
                messages.add("username cannot be shorter than $MINIMAL_USERNAME_LENGTH characters")
            }
            if (request.username.firstOrNull()?.isLetter() == false) {
                messages.add("Username must begin with a letter")
            }
            if (request.username.endsWith('.') || request.username.endsWith('_')) {
                messages.add("Username must end with a letter or a digit")
            }
            if (request.username.contains("..") || request.username.contains("__")) {
                messages.add("Username cannot contain consecutive dots or underscores")
            }

            // password checks
            if (request.password.length < MINIMAL_PASSWORD_LENGTH) {
                messages.add("password cannot be shorter than $MINIMAL_PASSWORD_LENGTH characters")
            }
            if (!request.password.any { it.isUpperCase() }) {
                messages.add("password must contain an uppercase letter")
            }
            if (!request.password.any { it.isLowerCase() }) {
                messages.add("password must contain a lowercase letter")
            }
            if (!request.password.any { it.isDigit() }) {
                messages.add("password must contain a digit")
            }
            if (!request.password.any { !it.isLetterOrDigit() }) {
                messages.add("password must contain a special character")
            }

            val message = messages.joinToString(", ")
            return if (message.isEmpty()) {
                VerifyResult(
                    success = true,
                    message = message
                )
            } else {
                VerifyResult(
                    success = false,
                    message = message
                )
            }
        }
    }
}