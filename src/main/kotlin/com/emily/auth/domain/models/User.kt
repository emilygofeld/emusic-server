package com.emily.auth.domain.models

data class User(
    val username: String,
    val password: String,
    val salt: String,
    val id: String? = null
)
