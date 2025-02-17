package com.emily.core.constants

import kotlinx.serialization.Serializable

@Serializable
object ProtocolCode {
    const val CREATE_PLAYLIST = 100
    const val DELETE_PLAYLIST = 110
    const val ADD_SONG_TO_PLAYLIST = 120
    const val DELETE_SONG_FROM_PLAYLIST = 130
    const val GET_PLAYLIST = 140
    const val GET_USER_PLAYLISTS = 150
    const val GET_CURR_USER_PLAYLISTS = 160
    const val ADD_SONG_TO_FAVORITES = 170
    const val DELETE_SONG_FROM_FAVORITES = 180
    const val UPDATE_PLAYLIST = 190
    const val GET_SONG = 200
    const val GET_USER_DATA = 300
    const val GET_CURR_USER_DATA = 310
    const val SUCCESS = 400
    const val ERROR = 500
    const val GET_SEARCH_RESULTS = 600
}
