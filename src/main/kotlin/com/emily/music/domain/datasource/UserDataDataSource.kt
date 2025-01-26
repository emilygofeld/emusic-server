package com.emily.music.domain.datasource

import com.emily.core.constants.ID
import com.emily.music.domain.models.UserData

interface UserDataDataSource {
    suspend fun insertUserData(userId: ID): Boolean
    suspend fun getUserData(userId: ID): UserData?
    suspend fun updateUserData(userData: UserData): Boolean
}