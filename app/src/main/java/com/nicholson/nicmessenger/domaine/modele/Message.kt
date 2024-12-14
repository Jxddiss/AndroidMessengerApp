package com.nicholson.nicmessenger.domaine.modele

import java.time.LocalDateTime

data class Message(
    val id : Long,
    val contenu : String,
    val date : LocalDateTime,
    val nomSender : String,
    val type : String,
    val style : String,
    val winkName : String
)