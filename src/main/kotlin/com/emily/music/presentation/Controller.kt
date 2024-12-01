package com.emily.music.presentation

import com.emily.music.domain.models.Playlist
import com.emily.music.domain.repository.MusicRepository
import com.emily.music.presentation.communication.MusicRequest
import com.emily.music.presentation.communication.MusicResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Controller(
    private val musicRepository: MusicRepository,
    private val call: RoutingCall
) {

    private suspend fun RoutingCall.sendResponse(response: MusicResponse) {
        respond(message = Json.encodeToString(response))
    }

    private suspend fun RoutingCall.sendError(message: String) {
        val response: MusicResponse = MusicResponse.ErrorResponse(message = message)
        respond(message = Json.encodeToString(response))
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