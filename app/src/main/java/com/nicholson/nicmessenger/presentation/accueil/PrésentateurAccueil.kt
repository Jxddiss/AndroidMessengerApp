package com.nicholson.nicmessenger.presentation.accueil

import android.util.Log
import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.accueil.ContratVuePrésentateurAccueil.*
import com.nicholson.nicmessenger.presentation.otd.ConversationItemOTD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurAccueil( private val vue : IVueAccueil,
                           private val iocontext : CoroutineContext = Dispatchers.IO )
    : IPrésentateurAccueil {

    private var job : Job? = null
    private lateinit var modèle : IModèle
    private var conversations : List<Conversation> = listOf()

    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
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
                    modèle.envoyerStatut()
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
        modèle.seDéconnecter()
        vue.redirigerÀLogin()
    }

    override fun attendreStatus() {
        job = CoroutineScope( iocontext ).launch {
            try {
                conversations.forEachIndexed { index, conversation ->
                    val idAutreUtilisateur = conversation.utilisateurs.firstOrNull {
                        it.id != modèle.utilisateurConnecté?.id
                    }?.id
                    idAutreUtilisateur?.let { id ->
                        Log.d( "Sub presenter", "/topic/user/status/$id" )
                        launch {
                            modèle.subscribeStatus( "/topic/user/status/$id" ).collect{
                                Log.d( "Status received presenter", it )
                                modèle.mettreÀJourStatusAmi( it, index )
                                CoroutineScope( Dispatchers.Main ).launch {
                                    vue.mettreÀJourStatus( index, it )
                                }
                            }
                        }
                    }
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