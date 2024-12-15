package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.domaine.service.exceptions.EmailInvalideException
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesUtilisateurFictive

class Authentification {
    companion object {
        private val EMAIL_REGEX : Regex =
            Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|" +
                    "\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-" +
                    "\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                    "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
                    "[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]" +
                    "*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\" +
                    "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")


        var sourceDeDonnées : ISourceDeDonéesUtilisateur = SourceDeDonnéesUtilisateurFictive()

        suspend fun seConnecter( email : String, motDePasse : String ) : Utilisateur {
            if( !EMAIL_REGEX.matches( email ) ) throw EmailInvalideException( "Email Invalide" )
            return sourceDeDonnées.seConnecter( email, motDePasse )
        }

        suspend fun demandeMotDePasseOublié( email : String ) {
            if( !EMAIL_REGEX.matches( email ) ) throw EmailInvalideException( "Email Invalide" )
            sourceDeDonnées.demandeMotDePasseOublié( email )
        }
    }
}