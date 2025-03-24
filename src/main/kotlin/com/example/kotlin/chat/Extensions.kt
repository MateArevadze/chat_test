package com.example.kotlin.chat

import com.example.kotlin.chat.model.ContentType
import com.example.kotlin.chat.model.Message
import com.example.kotlin.chat.model.MessageVM
import com.example.kotlin.chat.model.UserVM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.URL

fun MessageVM.asDomainObject(contentType: ContentType = ContentType.MARKDOWN): Message = Message(
    content,
    contentType,
    sent,
    user.name,
    user.avatarImageLink.toString(),
    id
)

fun String.toAsterisks() : String = this[0] + "****" + this[this.length-1]


fun Message.asViewModel(): MessageVM = MessageVM(
    content,
    UserVM(username.toAsterisks(), URL(userAvatarImageLink)),
    sent,
    id
)

fun Flow<Message>.mapToViewModel(): Flow<MessageVM> = map { it.asViewModel() }

fun List<Message>.mapToViewModel(): List<MessageVM> = map { it.asViewModel() }