package com.emily.di

import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val appModule = module {
    single<CoroutineDatabase> {
        val dbName = "emusic_db"
        KMongo.createClient(connectionString = "mongodb://localhost:27017")
            .coroutine.getDatabase(dbName)
    }
}