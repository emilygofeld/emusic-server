package com.emily.music.presentation.communication

import com.emily.core.ID
import com.emily.core.ProtocolCode
import kotlinx.serialization.Serializable

@Serializable
sealed class MusicRequests {
    abstract val code: Int

    @Serializable
    data class CreatePlaylist(override val code: Int = ProtocolCode.CREATE_PLAYLIST, val title: String): MusicRequests()
    @Serializable
    data class DeletePlaylist(override val code: Int = ProtocolCode.DELETE_PLAYLIST, val playlistId: ID): MusicRequests()
    @Serializable
    data class AddSongToPlaylist(override val code: Int = ProtocolCode.ADD_SONG_TO_PLAYLIST, val songId: ID, val playlistId: ID): MusicRequests()
    @Serializable
    data class DeleteSongFromPlaylist(override val code: Int = ProtocolCode.DELETE_SONG_FROM_PLAYLIST, val songId: ID, val playlistId: ID): MusicRequests()
    @Serializable
    data class GetPlaylist(override val code: Int = ProtocolCode.GET_PLAYLIST, val playlistId: ID): MusicRequests()
    @Serializable
    data class GetUserPlaylists(override val code: Int = ProtocolCode.GET_USER_PLAYLISTS, val userId: ID): MusicRequests()
    @Serializable
    data class GetSong(override val code: Int = ProtocolCode.GET_SONG, val songId: ID): MusicRequests()
    @Serializable
    data class GetUserData(override val code: Int = ProtocolCode.GET_USER_DATA, val userId: ID): MusicRequests()
    @Serializable
    data class GetCurrUserData(override val code: Int = ProtocolCode.GET_CURR_USER_DATA): MusicRequests()
    @Serializable
    data class GetCurrUserPlaylists(override val code: Int = ProtocolCode.GET_CURR_USER_PLAYLISTS): MusicRequests()
}

