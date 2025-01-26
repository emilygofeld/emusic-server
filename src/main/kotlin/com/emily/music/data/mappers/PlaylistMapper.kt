package com.emily.music.data.mappers

import com.emily.music.data.entity.PlaylistEntity
import com.emily.music.domain.models.Playlist

fun PlaylistEntity.toPlaylist(): Playlist {
    return Playlist(
        title = title,
        songs = songs,
        ownerName = ownerName,
        ownerId = ownerId,
        id = id
    )
}

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        title = title,
        songs = songs,
        ownerName = ownerName,
        ownerId = ownerId
    )
}