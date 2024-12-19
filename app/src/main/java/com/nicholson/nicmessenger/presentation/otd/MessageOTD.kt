package com.nicholson.nicmessenger.presentation.otd

data class MessageOTD(
    val contenu : String,
    val date : String,
    val avatar : String,
    val nomSender : String,
    val color : String?,
    val fontSize : Float?
)