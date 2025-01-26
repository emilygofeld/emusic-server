package com.emily.music.presentation

import com.emily.music.domain.models.Playlist
import com.emily.music.domain.repository.MusicRepository
import com.emily.music.presentation.communication.MusicRequest
import com.emily.music.presentation.communication.MusicResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast


class Controller(
    private val musicRepository: MusicRepository,
    private val call: RoutingCall
) {
    private suspend inline fun <reified T> RoutingCall.sendResponse(response: T) {
        val json = Json { encodeDefaults = true }
        respond(message = json.encodeToString(response))
    }

    private suspend fun RoutingCall.sendError(message: String) {
        val json = Json { encodeDefaults = true }
        respond(message = json.encodeToString(MusicResponse.ErrorResponse(message = message)))
    }

    private suspend inline fun <reified T : MusicResponse> RoutingCall.check(response: MusicResponse, type: KClass<T>) {
        val json = Json { encodeDefaults = true }
        val castedResponse = type.safeCast(response)
        if (castedResponse != null) {
            respond(castedResponse)
        } else {
            respond("Invalid type")
        }
    }

    suspend fun addSongToPlaylist(
        request: MusicRequest.AddSongToPlaylist,
    ) {
        if (!musicRepository.addSongToPlaylist(request.songId, request.playlistId))
            call.sendError("Server Error")
        else
            call.sendResponse(MusicResponse.SuccessResponse(data = null))
    }

    suspend fun createPlaylist(
        title: String,
        userId: String,
    ) {
        val playlistId = musicRepository.createPlaylistForUser(
            Playlist(title = title, ownerId = userId),
            userId
        )

        call.check(
            MusicResponse.SuccessResponse(data = "Check Worked"),
            type = MusicResponse.SuccessResponse::class
        )

        if (playlistId == null)
            call.sendError("Server Error")
        else
            call.sendResponse(MusicResponse.SuccessResponse(data = playlistId))
    }

    suspend fun deletePlaylist(
        playlistId: String,
        userId: String,
    ) {
        if (!musicRepository.removePlaylistFromUser(playlistId, userId))
            call.sendError("Server Error")
        else
            call.sendResponse(MusicResponse.SuccessResponse(data = null))
    }

    suspend fun deleteSongFromPlaylist(
        request: MusicRequest.DeleteSongFromPlaylist,
    ) {
        if (!musicRepository.removeSongFromPlaylist(request.songId, request.playlistId))
            call.sendError("Server Error")
        else
            call.sendResponse(MusicResponse.SuccessResponse(data = null))
    }

    suspend fun getUserData(
        userId: String,
    ) {
        val userData = musicRepository.getUserData(userId)

        if (userData == null)
            call.sendError("Server Error")
        else
            call.sendResponse(MusicResponse.GetUserData(userData = userData))
    }

    suspend fun getUserPlaylists(
        userId: String,
    ) {
        val playlists = musicRepository.getUserPlaylists(userId)
        call.sendResponse(MusicResponse.GetUserPlaylists(playlists = playlists))
    }

    suspend fun getPlaylist(
        playlistId: String,
    ) {
        val playlist = musicRepository.getPlaylist(playlistId)

        if (playlist == null)
            call.sendError("Server Error: Incorrect Id")
        else
            call.sendResponse(MusicResponse.GetPlaylist(playlist = playlist))
    }

    suspend fun getSong(
        songId: String,
    ) {
        val song = musicRepository.getSong(songId)

        if (song == null)
            call.sendError("Server Error: Incorrect id")
        else
            call.sendResponse(MusicResponse.GetSong(song = song))
    }
}