package com.emily.music.data.datasource

import com.emily.core.ID
import com.emily.music.data.entity.UserDataEntity
import com.emily.music.data.mappers.toUserData
import com.emily.music.data.mappers.toUserDataEntity
import com.emily.music.domain.datasource.UserDataDataSource
import com.emily.music.domain.models.UserData
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataDataSource(
    db: CoroutineDatabase
): UserDataDataSource {

    private val userDataCollection = db.getCollection<UserDataEntity>(collectionName = "UserData")

    override suspend fun getUserData(userId: ID): UserData? {
        return userDataCollection.findOne(UserDataEntity::id eq userId)?.toUserData()
    }

    override suspend fun updateUserData(userData: UserData): Boolean {
        return userDataCollection.replaceOne(UserDataEntity::id eq userData.id, userData.toUserDataEntity()).wasAcknowledged()
    }
}