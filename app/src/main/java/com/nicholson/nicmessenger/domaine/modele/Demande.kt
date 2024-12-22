package com.nicholson.nicmessenger.domaine.modele

data class Demande(
    val id : Long,
    val envoyeur : Utilisateur,
    var statut : String,
    var accepter : Boolean
)
