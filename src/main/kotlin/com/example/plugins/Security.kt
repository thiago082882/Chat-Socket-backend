package com.example.plugins

import com.example.sessions.ChatSession
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<ChatSession>("SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
  intercept(Plugins){

      if(call.sessions.get<ChatSession>()==null){
          val username = call.parameters["username"]?: "Thiago"
          call.sessions.set(ChatSession(username, generateNonce()))
      }

  }
}
