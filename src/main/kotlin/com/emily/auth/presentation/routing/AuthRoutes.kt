package com.emily.auth.presentation.routing

import com.emily.auth.domain.models.User
import com.emily.auth.domain.models.UserDataSource
import com.emily.auth.domain.security.hashing.HashingService
import com.emily.auth.domain.security.hashing.SaltedHash
import com.emily.auth.domain.security.token.TokenClaim
import com.emily.auth.domain.security.token.TokenConfig
import com.emily.auth.domain.security.token.TokenService
import com.emily.auth.domain.verify.Verify
import com.emily.auth.presentation.auth.AuthRequest
import com.emily.auth.presentation.auth.AuthResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signUp") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val verify = Verify.verifySignUp(request)
        if (!verify.success) {
            call.respond(HttpStatusCode.BadRequest, AuthResponse.ErrorResponse(message = verify.message))
            return@post
        }

        if (userDataSource.getUserByUsername(request.username) != null) {
            call.respond(HttpStatusCode.BadRequest, AuthResponse.ErrorResponse(message = "user taken"))
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        if (!userDataSource.insertUser(user)) {
            call.respond(HttpStatusCode.InternalServerError)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signIn") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username) ?: kotlin.run {
            call.respond(
                HttpStatusCode.BadRequest,
                "user not found"
            )
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) {
            call.respond(
                HttpStatusCode.BadRequest,
                "Incorrect username or password"
            )
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id!!
            )
        )

        call.respond(
            HttpStatusCode.OK,
            AuthResponse.SuccessResponse(
                token = token
            )
        )

    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}
