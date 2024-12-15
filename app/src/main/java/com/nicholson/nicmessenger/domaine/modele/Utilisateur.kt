package com.nicholson.nicmessenger.domaine.modele

data class Utilisateur(
    val id : Long,
    val nomComplet : String,
    val email : String,
    val statut : String,
    val description : String,
    val avatar : String,
    val banniere : String )
