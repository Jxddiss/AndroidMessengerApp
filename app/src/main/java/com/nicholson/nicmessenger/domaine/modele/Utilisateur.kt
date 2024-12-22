package com.nicholson.nicmessenger.domaine.modele

data class Utilisateur(
    val id : Long,
    val nomComplet : String,
    val email : String,
    var statut : String,
    var description : String,
    val avatar : String,
    val banniere : String )
