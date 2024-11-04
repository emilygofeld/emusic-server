package com.emily.auth.data.mappers

import com.emily.auth.data.entity.UserEntity
import com.emily.auth.domain.models.User

fun UserEntity.toUser(): User {
    return User(
        username = username,
        password = password,
        salt = salt,
        id = id
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        username = username,
        password = password,
        salt = salt
    )
}