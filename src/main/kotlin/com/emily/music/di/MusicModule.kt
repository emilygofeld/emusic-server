package com.emily.music.di

import com.emily.music.data.datasource.MongoPlaylistDataSource
import com.emily.music.data.datasource.MongoSongDataSource
import com.emily.music.data.datasource.MongoUserDataDataSource
import com.emily.music.data.repository.MusicRepositoryImpl
import com.emily.music.domain.datasource.PlaylistDataSource
import com.emily.music.domain.datasource.SongDataSource
import com.emily.music.domain.datasource.UserDataDataSource
import com.emily.music.domain.repository.MusicRepository
import com.emily.music.presentation.Controller
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase

val musicModule = module {
    single<UserDataDataSource> { MongoUserDataDataSource(get<CoroutineDatabase>()) }
    single<PlaylistDataSource> { MongoPlaylistDataSource(get<CoroutineDatabase>()) }
    single<SongDataSource> { MongoSongDataSource(get<CoroutineDatabase>()) }

    single<MusicRepository> {MusicRepositoryImpl(get<PlaylistDataSource>(), get<UserDataDataSource>(), get<SongDataSource>())}

    factory<Controller> { (call: RoutingCall) -> Controller(get<MusicRepository>(), call)}
}


