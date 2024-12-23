package com.nicholson.nicmessenger.presentation.accueil

import android.util.Log
import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.accueil.ContratVuePrésentateurAccueil.*
import com.nicholson.nicmessenger.presentation.otd.ConversationItemOTD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext

class PrésentateurAccueil( private val vue : IVueAccueil,
                           private val iocontext : CoroutineContext = Dispatchers.IO )
    : IPrésentateurAccueil {

    private var job : Job? = null
    private lateinit var modèle : IModèle
    private var conversations : List<Conversation> = listOf()

    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
        modèle.estSurVueNotifications = false
        modèle.nomConversationCourrante = ""
        vue.miseEnPlace()
    }

    override fun traiterObtenirConversation() {
        if( !modèle.estConnecté ) {
            modèle.cacherNav()
            vue.redirigerÀLogin()
        } else {
            vue.montrerChargement()
            job = CoroutineScope( iocontext ).launch {
                try {
                    conversations = modèle.obtenirMesConversations()
                    if ( !modèle.attendNotif ) {
                        modèle.attendreNotificationNav?.let { it() }
                    }
                } catch ( ex : AuthentificationException ) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.redirigerÀLogin()
                    }
                } catch ( ex : SourceDeDonnéesException ) {
                    Log.d( "Exception", "message : ${ex.message}" )
                }
                val conversationsOTDS = conversations.map {
                    convertirConversationAConversationOTD( it )
                }

                val notifications : List<Notification> = try {
                    modèle.obtenirNotificationsNonLu()
                } catch ( ex : SourceDeDonnéesException ) {
                    Log.d( "Exception", "message : ${ex.message}" )
                    emptyList()
                } catch ( ex : AuthentificationException ) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.redirigerÀLogin()
                    }
                    emptyList()
                }

                if ( notifications.isNotEmpty() && !modèle.indicateurNotifVisible ) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        modèle.montrerIndicateurNotif?.let { it() }
                    }
                }

                CoroutineScope( Dispatchers.Main ).launch {
                    vue.attacherListeConversationsRecycler( conversationsOTDS )
                    vue.masquerChargement()
                }
            }
        }
    }

    override fun traiterConversationCliquer( indice : Int ) {
        modèle.indiceConversationCourrante = indice
        vue.redirigerÀConversation()
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

    override fun attendreStatus() {
        job = CoroutineScope( iocontext ).launch {
            try {
                conversations.forEachIndexed { index, conversation ->
                    val idAutreUtilisateur = conversation.utilisateurs.firstOrNull {
                        it.id != modèle.utilisateurConnecté?.id
                    }?.id
                    idAutreUtilisateur?.let { id ->
                        launch {
                            modèle.subscribeStatus( "/topic/user/status/$id" ).collect{
                                modèle.mettreÀJourStatusAmi( it, index )
                                CoroutineScope( Dispatchers.Main ).launch {
                                    vue.mettreÀJourStatus( index, it )
                                }
                            }
                        }
                    }
                }
                if ( !modèle.statutOnlineConnexionEnvoyé ) {
                    delay( 500 )
                    modèle.envoyerStatut()
                    modèle.statutOnlineConnexionEnvoyé = true
                }
            } catch ( ex : SourceDeDonnéesException ) {
                Log.d( "Exception", "${ex.message}" )
            }
        }
    }

    private fun convertirConversationAConversationOTD( conversation: Conversation ) : ConversationItemOTD {
        val autreUtilisateur = conversation
            .utilisateurs.first { it.id != modèle.utilisateurConnecté?.id }

        return ConversationItemOTD(
            nomComplet = autreUtilisateur.nomComplet,
            statut = autreUtilisateur.statut,
            description = autreUtilisateur.description,
            avatar = autreUtilisateur.avatar
        )
    }
}