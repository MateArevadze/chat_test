package com.example.kotlin.chat

import com.example.kotlin.chat.model.Message
import com.example.kotlin.chat.model.MessageVM
import java.time.temporal.ChronoUnit.MILLIS

fun MessageVM.prepareForTesting() = copy(id = null, sent = sent.truncatedTo(MILLIS))

fun Message.prepareForTesting() = copy(id = null, timeSent = timeSent.truncatedTo(MILLIS))