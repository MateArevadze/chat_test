package com.example.kotlin.chat.model

import java.time.Instant

data class MessageVM(val content: String,
                     val gameId: String,
                     val user: UserVM,
                     val sent: Instant,
                     val id: String? = null)
