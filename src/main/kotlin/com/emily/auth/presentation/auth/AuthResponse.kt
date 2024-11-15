package com.emily.auth.presentation.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface AuthResponse {
    @Serializable
    @SerialName("Success")
    data class SuccessResponse(val token: String): AuthResponse
    @Serializable
    @SerialName("Error")
    data class ErrorResponse(val message: String): AuthResponse
}

val json = Json {
    serializersModule = SerializersModule {
        polymorphic(AuthResponse::class) {
            subclass(AuthResponse.SuccessResponse::class, AuthResponse.SuccessResponse.serializer())
            subclass(AuthResponse.ErrorResponse::class, AuthResponse.ErrorResponse.serializer())
        }
    }
    classDiscriminator = "type" // field tells the serializer weather it's an error or success response
}