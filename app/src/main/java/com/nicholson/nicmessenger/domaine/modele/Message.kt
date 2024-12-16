package com.nicholson.nicmessenger.domaine.modele

import java.time.LocalDateTime

data class Message(
    val id : Long,
    val contenu : String,
    val date : LocalDateTime,
    val nomSender : String,
    var type : String,
    var style : String?,
    val winkName : String?,
    val conversation: Conversation?
)