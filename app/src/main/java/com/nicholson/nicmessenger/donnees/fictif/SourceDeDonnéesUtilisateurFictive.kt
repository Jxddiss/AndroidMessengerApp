package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur
import com.nicholson.nicmessenger.donnees.exceptions.IdentifiantsException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import java.io.File

class SourceDeDonnéesUtilisateurFictive : ISourceDeDonéesUtilisateur {

    override suspend fun seConnecter( email: String, motDePasse: String ): Pair<String, Utilisateur> {
        if ( email != "email@email.com" || motDePasse != "password" ) {
            throw IdentifiantsException("L'email ou le mot de passe sont incorrect")
        }
        return "token" to FaussesDonnées.listeUtilisateur[0]
    }

    override suspend fun demandeMotDePasseOublié( email: String ) {  }
    override suspend fun mettreÀJourProfile( utilisateur: Utilisateur, avatarFile : File? ) : Utilisateur {
        return FaussesDonnées.listeUtilisateur[0]
    }
}