package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException

class SourceDeDonnéesUtilisateurFictive : ISourceDeDonéesUtilisateur {
    override suspend fun obtenirUtilisateurParId( id : Long ): Utilisateur {
        return FaussesDonnées.listeUtilisateur.firstOrNull{ it.id == id }
            ?: throw SourceDeDonnéesException("Utilisateur Inéxistant")
    }

    override suspend fun seConnecter( email: String, motDePasse: String ): Utilisateur {
        return FaussesDonnées.listeUtilisateur[0]
    }

    override suspend fun demandeMotDePasseOublié( email: String ) {  }
}