package com.nicholson.nicmessenger.presentation

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.domaine.service.Authentification

class Modèle private constructor() : IModèle {
    companion object {
        @Volatile
        private var instance : Modèle? = null

        fun obtenirInstance() =
            instance ?: synchronized(this) {
                instance ?: Modèle().also { instance = it }
            }
    }

    override var estConnecté = false
    override var utilisateurConnecté : Utilisateur? = null

    override suspend fun seConnecter( email: String, motDePasse: String ) {
        utilisateurConnecté = Authentification.seConnecter( email, motDePasse )
        estConnecté = true
    }

}