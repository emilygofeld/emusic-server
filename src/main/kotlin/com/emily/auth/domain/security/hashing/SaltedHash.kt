package com.emily.auth.domain.security.hashing

data class SaltedHash(
    val salt: String,
    val hash: String
)
