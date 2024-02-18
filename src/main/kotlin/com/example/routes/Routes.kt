package com.example.routes

import com.example.controller.ChatController
import com.example.controller.MemberAlreadyExistsException
import com.example.sessions.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocketRoute(chatController: ChatController) {
    webSocket("/chat-socket") {
        val session = call.sessions.get<ChatSession>()

        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session "))
            return@webSocket
        }

        try {

            chatController.onJoin(
                username = session.username,
                sessionId = session.sessionId,
                socket = this
            )

            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    chatController.sendMessage(session.username, frame.readText())

                }
            }
        }catch (e: MemberAlreadyExistsException) {

                call.respond(HttpStatusCode.Conflict)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                chatController.disconnect(session.username)
            }

        }
}
fun Route.getAllMessages(chatController: ChatController){
    get("/messages"){
call.respond(HttpStatusCode.OK,chatController.getAllMessages())
    }
}