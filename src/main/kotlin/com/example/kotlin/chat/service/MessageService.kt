package com.example.kotlin.chat.service

import com.example.kotlin.chat.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface MessageService {

    suspend fun latest(gameId: String): Flow<MessageDTO>

    suspend fun stream(): Flow<MessageDTO>

    suspend fun post(messages: Flow<MessageDTO>)

    suspend fun clean()
}