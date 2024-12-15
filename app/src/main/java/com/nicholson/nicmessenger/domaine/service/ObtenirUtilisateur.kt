package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesUtilisateurFictive

class ObtenirUtilisateur {
    companion object{
        var sourceDeDonnées : ISourceDeDonéesUtilisateur = SourceDeDonnéesUtilisateurFictive()

        suspend fun obtenirUtilisateurParId( id : Long ) : Utilisateur {
            return sourceDeDonnées.obtenirUtilisateurParId( id )
        }
    }
}