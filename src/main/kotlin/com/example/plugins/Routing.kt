package com.example.plugins

import com.example.chatController
import com.example.routes.chatSocketRoute
import com.example.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
      chatSocketRoute(chatController)
        getAllMessages(chatController)
    }
}
