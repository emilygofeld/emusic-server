package com.emily.auth.domain.security.token

data class TokenClaim(
    val name: String,
    val value: String
)
