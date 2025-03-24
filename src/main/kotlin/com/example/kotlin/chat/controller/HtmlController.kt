package com.example.kotlin.chat.controller

import com.example.kotlin.chat.service.MessageService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class HtmlController(val messageService: MessageService) {

    @GetMapping("/")
    fun index(): String {
        return "chatrs"
    }
    @PostMapping("/clean")
    suspend fun remove() {
        messageService.clean()
    }

}