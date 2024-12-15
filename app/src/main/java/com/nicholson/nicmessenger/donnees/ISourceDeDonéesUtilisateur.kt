package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Utilisateur

interface ISourceDeDonéesUtilisateur {
    suspend fun obtenirUtilisateurParId( id : Long ) : Utilisateur
    suspend fun seConnecter( email : String, motDePasse : String ) : Utilisateur
    suspend fun demandeMotDePasseOublié( email : String )
}