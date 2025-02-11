package com.emily.music.data.mappers

import com.emily.music.data.entity.PlaylistEntity
import com.emily.music.domain.models.Playlist

fun Playlist.toNewPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        title = title,
        songs = songs.map { song -> song.id },
        ownerName = ownerName,
        ownerId = ownerId
    )
}

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        title = title,
        songs = songs.map { song -> song.id },
        ownerName = ownerName,
        ownerId = ownerId,
        id = id
    )
}