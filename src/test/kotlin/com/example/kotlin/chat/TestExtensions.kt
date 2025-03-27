package com.example.kotlin.chat

import com.example.kotlin.chat.model.Message
import com.example.kotlin.chat.model.MessageDTO
import java.time.temporal.ChronoUnit.MILLIS

fun MessageDTO.prepareForTesting() = copy(id = null, sent = sent.truncatedTo(MILLIS))

fun Message.prepareForTesting() = copy(id = null, timeSent = timeSent.truncatedTo(MILLIS))