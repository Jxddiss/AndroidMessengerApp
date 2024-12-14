package com.nicholson.nicmessenger.domaine.modele

data class Conversation(
    val id : Long,
    val utilisateurs : Set<Utilisateur>
)
