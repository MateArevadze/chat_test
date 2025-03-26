package com.example.kotlin.chat

import com.example.kotlin.chat.model.Message
import com.example.kotlin.chat.model.MessageVM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.URL

fun MessageVM.asDomainObject(): Message = Message(
    userId,
    gameId,
    content,
    sent,
    id
)

fun String.toAsterisks() : String = this[0] + "****" + this[this.length-1]


fun Message.asViewModel(): MessageVM = MessageVM(
    content,
    gameId,
    userId,
    timeSent,
    id
)

fun Flow<Message>.mapToViewModel(): Flow<MessageVM> = map { it.asViewModel() }

