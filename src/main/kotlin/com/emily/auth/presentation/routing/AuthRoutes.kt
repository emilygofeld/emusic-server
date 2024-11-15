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
import com.emily.auth.presentation.auth.json
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


suspend fun RoutingCall.sendError(message: String) {
    val serializedResponse = json.encodeToString(AuthResponse.serializer(), AuthResponse.ErrorResponse(message))
    respond(
        status = HttpStatusCode.BadRequest,
        message = serializedResponse
    )
}

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signUp") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val verify = Verify.verifySignUp(request)
        if (!verify.success) {
            call.sendError(verify.message)
            return@post
        }

        if (userDataSource.getUserByUsername(request.username) != null) {
            call.sendError("username is already taken")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        var user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        if (!userDataSource.insertUser(user)) {
            call.respond(HttpStatusCode.InternalServerError)
            return@post
        }
        user = userDataSource.getUserByUsername(user.username)!!

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id!!
            )
        )

        // send success status
        call.respond(
            message = json.encodeToString(AuthResponse.serializer(), AuthResponse.SuccessResponse(token)),
            status = HttpStatusCode.OK
        )
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
            call.sendError("user not found")
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
            call.sendError("Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id!!
            )
        )

        // send success status
        call.respond(
            message = json.encodeToString(AuthResponse.serializer(), AuthResponse.SuccessResponse(token)),
            status = HttpStatusCode.OK
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

