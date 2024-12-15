package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesUtilisateurFictive

class Authentification {
    companion object {
        var sourceDeDonnées : ISourceDeDonéesUtilisateur = SourceDeDonnéesUtilisateurFictive()

        suspend fun seConnecter( email : String, motDePasse : String ) : Utilisateur {
            return sourceDeDonnées.seConnecter( email, motDePasse )
        }
    }
}