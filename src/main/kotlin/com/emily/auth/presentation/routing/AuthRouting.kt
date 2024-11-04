package com.emily.auth.presentation.routing

import com.emily.auth.domain.models.UserDataSource
import com.emily.auth.domain.security.hashing.HashingService
import com.emily.auth.domain.security.token.TokenConfig
import com.emily.auth.domain.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.authRouting(config: TokenConfig) {
    val userDataSource: UserDataSource by inject()
    val hashingService: HashingService by inject()
    val tokenService: TokenService by inject()

    routing {
        signUp(hashingService, userDataSource)
        signIn(hashingService, userDataSource, tokenService, config)
        authenticate()
    }
}