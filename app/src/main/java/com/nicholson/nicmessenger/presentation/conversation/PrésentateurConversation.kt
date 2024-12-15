package com.nicholson.nicmessenger.presentation.conversation

import android.util.Log
import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.conversation.ContratVuePrésentateurConversation.*
import com.nicholson.nicmessenger.presentation.otd.ConversationOTD
import com.nicholson.nicmessenger.presentation.otd.MessageOTD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

class PrésentateurConversation(
    private val vue : IVueConversation,
    private val iocontext : CoroutineContext = Dispatchers.IO
) : IPrésentateurConversation {
    private val modèle : IModèle = Modèle.obtenirInstance()
    private var job : Job? = null
    val formatterDate = DateTimeFormatter.ofPattern( "dd MMMM yyyy HH:MM" )
    var conversation : Conversation? = null

    override fun traiterDémarrage() {
        vue.montrerChargement()
        vue.miseEnPlace()
    }

    override fun traiterObtenirConversation() {
        job = CoroutineScope( iocontext ).launch {
            var messages : List<Message> = listOf()
            try {
                conversation = modèle.obtenirConversationCourrante()
                messages = modèle.obtenirMessagesPrécédent()
            } catch ( ex : AuthentificationException ) {
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.redirigerÀLogin()
                }
            } catch ( ex : SourceDeDonnéesException ) {
                Log.d("Exception : ", ex.message.toString())
            }

            conversation?.let { it ->
                val listMessageOTD = messages.map { message ->
                    convertirMessageÀMessageOTD( message )
                }
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.placerConversation( convertirConversationÀConversationOTD( it ) )
                    vue.placerMessagesPrécédents( listMessageOTD )
                    vue.masquerChargement()
                }
                try {
                    modèle.subscribeMessage("/topic/conversation/${it.id}").collect {
                            CoroutineScope( Dispatchers.Main ).launch {
                                vue.ajouterMessage( convertirMessageÀMessageOTD( it ) )
                            }
                    }
                }catch ( ex : SourceDeDonnéesException ) {
                    Log.d("Exception : ", ex.message.toString())
                }
            }
        }
    }

    private fun convertirConversationÀConversationOTD( conversation : Conversation ) : ConversationOTD {
        val autreUtilisateur = conversation
            .utilisateurs.first { it.id != modèle.utilisateurConnecté?.id }

        return ConversationOTD(
            nomComplet = autreUtilisateur.nomComplet,
            statut = autreUtilisateur.statut,
            description = autreUtilisateur.description,
            avatar = autreUtilisateur.avatar,
            bannière = autreUtilisateur.banniere
        )
    }

    private fun convertirMessageÀMessageOTD( message : Message ) : MessageOTD {
        val date = message.date.format( formatterDate )
        val autreAvatar = conversation
            ?.utilisateurs?.firstOrNull { it.nomComplet == message.nomSender }?.avatar ?: "buddy2"

        return MessageOTD(
            contenu = message.contenu,
            date = date,
            nomSender = message.nomSender,
            avatar = autreAvatar
        )
    }

}