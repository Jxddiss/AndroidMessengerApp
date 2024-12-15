package com.nicholson.nicmessenger.presentation

import com.nicholson.nicmessenger.domaine.modele.Utilisateur

interface IModèle {
    var estConnecté : Boolean
    var utilisateurConnecté : Utilisateur?
    suspend fun seConnecter( email : String, motDePasse : String )
    suspend fun demandeMotDePasseOublié( email : String )

}