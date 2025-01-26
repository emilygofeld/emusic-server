package com.emily.music.presentation.routing

import com.emily.music.domain.repository.MusicRepository
import com.emily.music.presentation.Controller
import com.emily.music.presentation.communication.MusicRequest
import com.emily.music.presentation.communication.MusicResponse
import com.emily.music.presentation.communication.RequestDeserializer
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.getAndDeserializeRequest(
    musicRepository: MusicRepository
) {
    authenticate {
        post("emusic") {
            val jsonRequest = call.receiveNullable<String>() ?: kotlin.run {
                call.respond(MusicResponse.ErrorResponse(message = "Invalid request"))
                return@post
            }

            val principal = call.principal<JWTPrincipal>()
            val connectedUserId = principal?.payload?.getClaim("userId")?.asString()

            if (connectedUserId == null) {
                call.respond(MusicResponse.ErrorResponse(message = "Invalid token"))
                return@post
            }

//            val controller by inject<Controller> {parametersOf(call)}
            val controller = Controller(musicRepository, call)

            println(Json.decodeFromString(RequestDeserializer, jsonRequest))

            when (val request = Json.decodeFromString(RequestDeserializer, jsonRequest)) {
                is MusicRequest.AddSongToPlaylist ->
                    controller.addSongToPlaylist(request)
                is MusicRequest.CreatePlaylist ->
                    controller.createPlaylist(request.title, connectedUserId)
                is MusicRequest.DeletePlaylist ->
                    controller.deletePlaylist(request.playlistId, connectedUserId)
                is MusicRequest.DeleteSongFromPlaylist ->
                    controller.deleteSongFromPlaylist(request)
                is MusicRequest.GetCurrUserData ->
                    controller.getUserData(connectedUserId)
                is MusicRequest.GetCurrUserPlaylists ->
                    controller.getUserPlaylists(connectedUserId)
                is MusicRequest.GetPlaylist ->
                    controller.getPlaylist(request.playlistId)
                is MusicRequest.GetSong ->
                    controller.getSong(request.songId)
                is MusicRequest.GetUserData ->
                    controller.getUserData(request.userId)
                is MusicRequest.GetUserPlaylists ->
                    controller.getUserPlaylists(request.userId)
            }
        }
    }
}

