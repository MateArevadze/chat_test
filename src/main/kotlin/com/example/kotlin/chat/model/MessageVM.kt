package com.example.kotlin.chat.model

import java.time.Instant

data class MessageVM(val content: String,
                     val gameId: String,
                     val userId: String,
                     val sent: Instant)

