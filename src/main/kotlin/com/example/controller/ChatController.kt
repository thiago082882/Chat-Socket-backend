package com.example.controller

import com.example.data.model.Message
import com.example.data.network.MessageDataSource
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private  val messageDataSource: MessageDataSource
) {

    private  val members = ConcurrentHashMap<String,Member>()

    fun onJoin(username :String , sessionId:String,socket:WebSocketSession){

       // Se existe um usuario, vai lançar a exceção
        if(members.contains(username)){
            throw MemberAlreadyExistsException()

        }

        members[username] = Member(username,sessionId, socket)


    }

    suspend fun sendMessage(senderUsername :String , message:String){
        val messageEntity = Message(message,senderUsername,System.currentTimeMillis())

        val parsedMessage = Json.encodeToString(messageEntity)

        members.values.forEach { member->
            member.socket.send(Frame.Text(parsedMessage))
        }
        messageDataSource.insertMessage(messageEntity)
    }

    suspend fun getAllMessages():List<Message>{
        return  messageDataSource.getAllMessages()
    }

    suspend fun disconnect(username:String){
        members[username]?.socket?.close()
        if(members.contains(username)){
            members.remove(username)
        }
    }


}