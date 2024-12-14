package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur

class SourceDeDonnéesUtilisateurFictive : ISourceDeDonéesUtilisateur {
    override suspend fun obtenirUtilisateurParId( id : Int ): Utilisateur {
        TODO("Not yet implemented")
    }
}