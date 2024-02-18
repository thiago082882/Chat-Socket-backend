package com.example.data.model

import de.undercouch.bson4jackson.types.ObjectId
import de.undercouch.bson4jackson.types.Timestamp
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Message(
    val text : String,
    val username : String ,
    val timestamp: Long,
    @BsonId
    val id :String = org.bson.types.ObjectId().toString()
)
