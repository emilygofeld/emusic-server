package com.emily.music.presentation

import com.emily.core.constants.ID
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
    private suspend inline fun <reified T> RoutingCall.sendResponse(response: T) {
        val json = Json { encodeDefaults = true }
        respond(message = json.encodeToString(response))
    }

    private suspend fun RoutingCall.sendError(message: String) {
        val json = Json { encodeDefaults = true }
        respond(message = json.encodeToString(MusicResponse.ErrorResponse(message = message)))
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
        username: String
    ) {
        val playlistId = musicRepository.createPlaylistForUser(
            Playlist(title = title, ownerName = username, ownerId = userId),
            userId
        )

        call.sendResponse(MusicResponse.SuccessResponse(data = null))

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
            call.sendError("Server Error: getting user data")
        else
            call.sendResponse(MusicResponse.GetUserData(userData = userData))
    }

    suspend fun getUserPlaylists(
        userId: ID,
    ) {
        val playlists = musicRepository.getUserPlaylists(userId)
        call.sendResponse(MusicResponse.GetUserPlaylists(playlists = playlists))
    }

    suspend fun getPlaylist(
        playlistId: ID,
        userId: ID
    ) {
        val playlist = musicRepository.getPlaylist(playlistId, userId)

        if (playlist == null)
            call.sendError("Server Error: Incorrect Id")
        else
            call.sendResponse(MusicResponse.GetPlaylist(playlist = playlist))
    }

    suspend fun getSong(
        songId: ID,
        userId: ID
    ) {
        val song = musicRepository.getSong(songId, userId)

        if (song == null)
            call.sendError("Server Error: Incorrect id")
        else
            call.sendResponse(MusicResponse.GetSong(song = song))
    }

    suspend fun addSongToFavorites(songId: ID, userId: ID) {
        if (!musicRepository.addSongToFavorites(songId, userId))
            call.sendError("Server Error: adding song to favorites")
        else
            call.sendResponse(MusicResponse.SuccessResponse(data = null))
    }

    suspend fun removeSongFromFavorites(songId: ID, userId: ID) {
        if (!musicRepository.removeSongFromFavorites(songId, userId))
            call.sendError("Server Error: removing song from favorites")
        else
            call.sendResponse(MusicResponse.SuccessResponse(data = null))
    }

    suspend fun updatePlaylist(playlist: Playlist) {
        if (!musicRepository.updatePlaylist(playlist))
            call.sendError("Server Error: updating playlist")
        else
            call.sendResponse(MusicResponse.SuccessResponse(data = null))
    }
}