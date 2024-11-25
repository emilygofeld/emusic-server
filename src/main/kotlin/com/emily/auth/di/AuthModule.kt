package com.emily.auth.di

import com.emily.auth.data.hashing.SHA256HashingService
import com.emily.auth.data.models.MongoUserDataSource
import com.emily.auth.data.token.JwtTokenService
import com.emily.auth.domain.models.UserDataSource
import com.emily.auth.domain.security.hashing.HashingService
import com.emily.auth.domain.security.token.TokenService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val authModule = module {
   single<UserDataSource> { MongoUserDataSource(get<CoroutineDatabase>()) }
   single<TokenService> { JwtTokenService() }
   single<HashingService> { SHA256HashingService() }
}

