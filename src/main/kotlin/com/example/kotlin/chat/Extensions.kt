package com.example.kotlin.chat

import com.example.kotlin.chat.model.Message
import com.example.kotlin.chat.model.MessageDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun MessageDTO.asDomainObject(): Message = Message(
    userId,
    gameId,
    content,
    sent
)

fun String.toAsterisks() : String = this[0] + "****" + this[this.length-1]


fun Message.asDTO(): MessageDTO = MessageDTO(
    content,
    gameId,
    userId ,
    timeSent
)

fun Flow<Message>.mapToDTO(): Flow<MessageDTO> = map { it.asDTO() }

