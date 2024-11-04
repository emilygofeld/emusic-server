package com.emily.auth.di

import com.emily.auth.data.hashing.SHA256HashingService
import com.emily.auth.data.models.MongoUserDataSource
import com.emily.auth.data.token.JwtTokenService
import com.emily.auth.domain.models.UserDataSource
import com.emily.auth.domain.security.hashing.HashingService
import com.emily.auth.domain.security.token.TokenService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val authModule = module {
   single<CoroutineDatabase> {
      val dbName = "spotify-users"
      KMongo.createClient(connectionString = "mongodb://localhost:27017")
         .coroutine.getDatabase(dbName)
   }
   single<UserDataSource> { MongoUserDataSource(get<CoroutineDatabase>()) }
   single<TokenService> { JwtTokenService() }
   single<HashingService> { SHA256HashingService() }
}

