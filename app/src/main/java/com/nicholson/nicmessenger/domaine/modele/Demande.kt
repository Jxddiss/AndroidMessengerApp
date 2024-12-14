package com.nicholson.nicmessenger.domaine.modele

data class Demande(
    val id : Long,
    val envoyeur : Utilisateur,
    val statut : String,
    val accepter : Boolean
)
