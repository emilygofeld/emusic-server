package com.emily.auth.presentation.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthResponse {
    @Serializable
    @SerialName("Success")
    data class SuccessResponse(val token: String)
    @Serializable
    @SerialName("Error")
    data class ErrorResponse(val message: String)
}




