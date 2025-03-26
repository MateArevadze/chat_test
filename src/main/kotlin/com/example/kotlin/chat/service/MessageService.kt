package com.example.kotlin.chat.service

import com.example.kotlin.chat.model.MessageVM
import kotlinx.coroutines.flow.Flow

interface MessageService {

    suspend fun latest(gameId: String): Flow<MessageVM>

    suspend fun stream(): Flow<MessageVM>

    suspend fun post(messages: Flow<MessageVM>)

    suspend fun clean()
}