package com.emily.music.data.mappers

import com.emily.music.data.entity.SongEntity
import com.emily.music.domain.models.Song

fun SongEntity.toSong(): Song {
    return Song(
        title = title,
        artists = artists,
        length = length,
        isFavorite = isFavorite,
        url = url,
        id = id
    )
}

fun Song.toSongEntity(): SongEntity {
    return SongEntity(
        title = title,
        artists = artists,
        length = length,
        isFavorite = isFavorite,
        url = url,
        id = id
    )
}