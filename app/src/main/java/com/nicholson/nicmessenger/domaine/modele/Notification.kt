package com.nicholson.nicmessenger.domaine.modele

data class Notification(
    val id : Long,
    val titre : String,
    val message : String,
    val image : String,
    val type : String,
    val lu : Boolean
)
