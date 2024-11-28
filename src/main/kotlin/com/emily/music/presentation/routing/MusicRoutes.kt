package com.emily.music.presentation.routing

import com.emily.core.ProtocolCode
import com.emily.music.domain.datasource.PlaylistDataSource
import com.emily.music.domain.datasource.SongDataSource
import com.emily.music.domain.datasource.UserDataDataSource
import com.emily.music.presentation.communication.RequestDeserializer
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.getAndDeserializeRequest(
    playlistDataSource: PlaylistDataSource,
    songDataSource: SongDataSource,
    userDataDataSource: UserDataDataSource
) {
    post("emusic") {
        val jsonRequest = call.receiveNullable<String>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val deserializedRequest = Json.decodeFromString(RequestDeserializer, jsonRequest)
        when (deserializedRequest.code) {
            ProtocolCode.CREATE_PLAYLIST -> {
                // Handle CREATE_PLAYLIST
            }
            ProtocolCode.DELETE_PLAYLIST -> {
                // Handle DELETE_PLAYLIST
            }
            ProtocolCode.ADD_SONG_TO_PLAYLIST -> {
                // Handle ADD_SONG_TO_PLAYLIST
            }
            ProtocolCode.DELETE_SONG_FROM_PLAYLIST -> {
                // Handle DELETE_SONG_FROM_PLAYLIST
            }
            ProtocolCode.GET_PLAYLIST -> {
                // Handle GET_PLAYLIST
            }
            ProtocolCode.GET_USER_PLAYLISTS -> {
                // Handle GET_USER_PLAYLISTS
            }
            ProtocolCode.GET_CURR_USER_PLAYLISTS -> {
                // Handle GET_CURR_USER_PLAYLISTS
            }
            ProtocolCode.GET_SONG -> {
                // Handle GET_SONG
            }
            ProtocolCode.GET_USER_DATA -> {
                // Handle GET_USER_DATA
            }
            ProtocolCode.GET_CURR_USER_DATA -> {
                // Handle GET_CURR_USER_DATA
            }
            ProtocolCode.SUCCESS -> {
                // Handle SUCCESS
            }
            ProtocolCode.ERROR -> {
                // Handle ERROR
            }
            else -> {
                // Handle unknown or unhandled cases
                throw IllegalArgumentException("Unknown code: ${deserializedRequest.code}")
            }
        }
    }
}