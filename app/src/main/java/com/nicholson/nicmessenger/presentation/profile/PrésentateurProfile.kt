package com.nicholson.nicmessenger.presentation.profile

import android.util.Log
import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.otd.UtilisateurOTD
import com.nicholson.nicmessenger.presentation.profile.ContratVuePrésentateurProfile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurProfile( val vue : IVueProfile,
                           private val iocontext : CoroutineContext = Dispatchers.IO )
    : IPrésentateurProfile {

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
        modèle.estSurVueNotifications = false
        modèle.nomConversationCourrante = ""
        vue.miseEnPlace()
    }

    override fun traiterObtenirUtilisateurConnecté() {
        modèle.utilisateurConnecté?.let {
            vue.placerUtilisateur( convertirUtilisateurÀUtilisateurOTD( it ) )
        }
    }

    override fun mettreÀJourProfile() {
        val description = vue.obtenirDescription()
        val status = vue.obtenirStatus()
        val avatarFile = vue.obtenirNouvelAvatar()
        val bannièreFile = vue.obtenirNouvelleBannière()

        if ( description.isNotEmpty()
            && status.isNotEmpty()
            && ( description != modèle.utilisateurConnecté?.description
                 || status != modèle.utilisateurConnecté?.statut
                 || avatarFile != null
                 || bannièreFile != null ) ) {

            modèle.utilisateurConnecté?.statut = vue.obtenirStatus()
            modèle.utilisateurConnecté?.description = vue.obtenirDescription()
            job = CoroutineScope( iocontext ).launch {
                try {
                    modèle.currentStatus = status
                    modèle.mettreÀJourProfile( avatarFile, bannièreFile )
                    modèle.envoyerStatut()
                } catch ( ex : AuthentificationException) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.redirigerÀLogin()
                    }
                } catch ( ex : SourceDeDonnéesException) {
                    Log.d( "Exception", "message : ${ex.message}" )
                }
                CoroutineScope( Dispatchers.Main ).launch {
                    traiterObtenirUtilisateurConnecté()
                }
            }
        }
    }

    override fun traiterDeconnexion() {
        job = CoroutineScope( iocontext ).launch {
            modèle.currentStatus = "disconnected"
            try {
                modèle.envoyerStatut()
            } catch ( ex : SourceDeDonnéesException ) {
                Log.d( "Exception", "message : ${ex.message}" )
            }

            CoroutineScope( Dispatchers.Main ).launch {
                modèle.seDéconnecter()
                vue.redirigerÀLogin()
            }
        }
    }

    override fun traiterOuvrirGalleriePourAvatar() {
        vue.ouvrirGalleriePhotoPourAvatar()
    }

    override fun traiterOuvrirGalleriePourBannière() {
        vue.ouvrirGalleriePhotoPourBannière()
    }

    private fun convertirUtilisateurÀUtilisateurOTD( utilisateur: Utilisateur ) : UtilisateurOTD =
        UtilisateurOTD(
            nomComplet = utilisateur.nomComplet,
            statut = utilisateur.statut,
            description = utilisateur.description,
            avatar = utilisateur.avatar,
            bannière = utilisateur.banniere
        )
}