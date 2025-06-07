package com.emily.music.presentation.communication

import com.emily.core.constants.ProtocolCode
import com.emily.music.domain.models.Playlist
import com.emily.music.domain.models.Song
import com.emily.music.domain.models.UserData
import kotlinx.serialization.Serializable

@Serializable
sealed class MusicResponse {
    abstract val code: Int

    // for success/error operations
    @Serializable
    data class SuccessResponse(override val code: Int = ProtocolCode.SUCCESS, val data: String?): MusicResponse()
    @Serializable
    data class ErrorResponse(override val code: Int = ProtocolCode.ERROR, val message: String): MusicResponse()

    @Serializable
    data class GetPlaylist(override val code: Int = ProtocolCode.GET_PLAYLIST, val playlist: Playlist): MusicResponse()
    @Serializable
    data class GetUserPlaylists(override val code: Int = ProtocolCode.GET_USER_PLAYLISTS, val playlists: List<Playlist>): MusicResponse()
    @Serializable
    data class GetSong(override val code: Int = ProtocolCode.GET_SONG, val song: Song): MusicResponse()
    @Serializable
    data class GetUserData(override val code: Int = ProtocolCode.GET_USER_DATA, val userData: UserData): MusicResponse()
    @Serializable
    data class GetSearchResult(override val code: Int = ProtocolCode.GET_SEARCH_RESULTS, val songs: List<Song>): MusicResponse()
    @Serializable
    data class GetGlobalFavoriteSongs(override val code: Int = ProtocolCode.GET_GLOBAL_FAVORITE_SONGS, val songs: List<Song>): MusicResponse()
}