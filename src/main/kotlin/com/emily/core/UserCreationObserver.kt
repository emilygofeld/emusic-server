package com.emily.core

import com.emily.core.constants.ID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.Flow

object UserCreationObserver {

    private val _eventFlow = MutableSharedFlow<UserCreatedEvent>()
    val eventFlow: Flow<UserCreatedEvent> = _eventFlow.asSharedFlow()

    suspend fun sendEvent(event: UserCreatedEvent) {
        _eventFlow.emit(event)
    }
}

data class UserCreatedEvent(val userId: ID, val username: String)
