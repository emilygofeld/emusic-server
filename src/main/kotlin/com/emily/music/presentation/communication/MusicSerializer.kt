package com.emily.music.presentation.communication

import com.emily.core.constants.ProtocolCode
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*

object RequestDeserializer: JsonContentPolymorphicSerializer<MusicRequest>(MusicRequest::class) {
    public override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MusicRequest> {
        return when (val code = element.jsonObject["code"]?.jsonPrimitive?.intOrNull) {
            ProtocolCode.DELETE_PLAYLIST -> MusicRequest.DeletePlaylist.serializer()
            ProtocolCode.GET_SONG -> MusicRequest.GetSong.serializer()
            ProtocolCode.GET_PLAYLIST -> MusicRequest.GetPlaylist.serializer()
            ProtocolCode.ADD_SONG_TO_PLAYLIST -> MusicRequest.AddSongToPlaylist.serializer()
            ProtocolCode.CREATE_PLAYLIST -> MusicRequest.CreatePlaylist.serializer()
            ProtocolCode.DELETE_SONG_FROM_PLAYLIST -> MusicRequest.DeleteSongFromPlaylist.serializer()
            ProtocolCode.GET_CURR_USER_DATA -> MusicRequest.GetCurrUserData.serializer()
            ProtocolCode.GET_CURR_USER_PLAYLISTS -> MusicRequest.GetCurrUserPlaylists.serializer()
            ProtocolCode.GET_USER_DATA -> MusicRequest.GetUserData.serializer()
            ProtocolCode.GET_USER_PLAYLISTS -> MusicRequest.GetUserPlaylists.serializer()
            ProtocolCode.ADD_SONG_TO_FAVORITES -> MusicRequest.AddSongToFavorites.serializer()
            ProtocolCode.DELETE_SONG_FROM_FAVORITES -> MusicRequest.DeleteSongFromPlaylist.serializer()
            else -> throw SerializationException("Unknown request code: $code")
        }
    }
}