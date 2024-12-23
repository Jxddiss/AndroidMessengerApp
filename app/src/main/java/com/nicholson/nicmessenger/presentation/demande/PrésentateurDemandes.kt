package com.nicholson.nicmessenger.presentation.demande

import android.util.Log
import com.nicholson.nicmessenger.domaine.modele.Demande
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.demande.ContratVuePrésentateurDemandes.*
import com.nicholson.nicmessenger.presentation.otd.DemandeOTD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurDemandes( val vue : IVueDemandes,
                            private val iocontext : CoroutineContext = Dispatchers.IO )
    : IPrésentateurDemandes {

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
        modèle.estSurVueNotifications = false
        vue.miseEnPlace()
        vue.montrerChargement()
    }

    override fun traiterObtenirDemandes() {
        if( !modèle.estConnecté ) {
            modèle.cacherNav()
            vue.redirigerÀLogin()
        } else {
            job = CoroutineScope( iocontext ).launch {
                val demandes : List<Demande> = try {
                    modèle.obtenirDemandes()
                } catch ( ex : SourceDeDonnéesException) {
                    Log.d( "Exception", "message : ${ex.message}" )
                    emptyList()
                } catch ( ex : AuthentificationException) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.redirigerÀLogin()
                    }
                    emptyList()
                }

                val demandesOTD = demandes.map { convertirDemandeÀDemandeOTD( it ) }
                val nom = modèle.utilisateurConnecté?.nomComplet ?: ""

                CoroutineScope( Dispatchers.Main ).launch {
                    vue.placerDemandes( demandesOTD, nom )
                    vue.masquerChargement()
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

    override fun accepterDemande( position: Int ) {
        job = CoroutineScope( iocontext ).launch {
            try {
                modèle.accepterDemande( position )
                traiterObtenirDemandes()
            } catch (ex: SourceDeDonnéesException) {
                Log.d("Exception", "message : ${ex.message}")
            } catch (ex: AuthentificationException) {
                CoroutineScope(Dispatchers.Main).launch {
                    vue.redirigerÀLogin()
                }
            }
        }
    }

    override fun refuserDemande( position: Int ) {
        job = CoroutineScope( iocontext ).launch {
            try {
                modèle.refuserDemande( position )
                traiterObtenirDemandes()
            } catch (ex: SourceDeDonnéesException) {
                Log.d("Exception", "message : ${ex.message}")
            } catch (ex: AuthentificationException) {
                CoroutineScope(Dispatchers.Main).launch {
                    vue.redirigerÀLogin()
                }
            }
        }
    }

    private fun convertirDemandeÀDemandeOTD( demande: Demande )
    : DemandeOTD =
        DemandeOTD(
            nomComplet = demande.envoyeur.nomComplet,
            image = demande.envoyeur.avatar,
            statut = demande.statut
        )

}