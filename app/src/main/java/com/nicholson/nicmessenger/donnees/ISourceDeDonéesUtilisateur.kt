package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Utilisateur

interface ISourceDeDonéesUtilisateur {
    suspend fun obtenirUtilisateurParId( id : Int ) : Utilisateur
}