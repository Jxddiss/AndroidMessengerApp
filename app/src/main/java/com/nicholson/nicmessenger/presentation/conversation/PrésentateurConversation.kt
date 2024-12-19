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
import kotlinx.coroutines.flow.catch
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
    var densitéÉcran : Float = 0.0F

    override fun traiterDémarrage() {
        vue.montrerChargement()
        vue.miseEnPlace()
        densitéÉcran = vue.obtenirDensitéÉcran()
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
                val listMessageOTD = messages.filter { message ->
                    message.type == "text"
                }.map { message -> convertirMessageÀMessageOTD( message ) }.takeLast( 20 )

                CoroutineScope( Dispatchers.Main ).launch {
                    vue.placerConversation( convertirConversationÀConversationOTD( it ) )
                    vue.placerMessagesPrécédents( listMessageOTD )
                    vue.masquerChargement()
                }
                modèle.subscribeMessage("/topic/conversation/${it.id}")
                    .catch {
                        Log.d("Exception : ", it.message.toString())
                    }.collect {
                        Log.d("Message received", "In presenter")
                        if( it.type == "text" ) {
                            CoroutineScope( Dispatchers.Main ).launch {
                                vue.ajouterMessage( convertirMessageÀMessageOTD( it ) )
                            }
                        } else if ( it.type == "nudge" && it.nomSender != modèle.utilisateurConnecté?.nomComplet ) {
                            CoroutineScope( Dispatchers.Main ).launch {
                                vue.wizz()
                            }
                        }
                    }
            }
        }
    }

    override fun traiterEnvoieMessage() {
        val messageContenu = vue.obtenirContenueMessage()
        if ( messageContenu.isNotEmpty() ){
            job = CoroutineScope( iocontext ).launch {
                conversation?.let {
                    envoyerMessage( it.id, messageContenu, "text" )
                }
            }
        }
    }

    override fun traiterEnvoieWizz() {
        job = CoroutineScope( iocontext ).launch {
            conversation?.let {
                envoyerMessage( it.id, "", "nudge" )
            }
        }
    }

    override fun attendreStatus() {
        job = CoroutineScope( iocontext ).launch {
            val idAutreUtilisateur = conversation?.utilisateurs?.firstOrNull {
                it.id != modèle.utilisateurConnecté?.id
            }?.id

            idAutreUtilisateur?.let { id ->
                modèle.subscribeStatus( "/topic/user/status/$id" )
                    .catch {
                        Log.d("Exception : ", it.message.toString())
                    }.collect{
                        CoroutineScope( Dispatchers.Main ).launch {
                            vue.mettreÀJourStatusAmi( it )
                        }
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

        val color = if ( message.style?.color != null && message.style?.color!!.isNotEmpty() ) {
            message.style?.color
        } else {
            null
        }

        val fontSize = if ( message.style?.fontSize != null && message.style?.fontSize!!.isNotEmpty() ) {
            val fontSizeString = message.style?.fontSize!!.removeSuffix("rem")
            fontSizeString.toFloatOrNull()?.times(16)?.div(densitéÉcran)
        } else {
            null
        }

        return MessageOTD(
            contenu = message.contenu,
            date = date,
            nomSender = message.nomSender,
            avatar = autreAvatar,
            color = color,
            fontSize = fontSize
        )
    }

    private suspend fun envoyerMessage( idConv : Long, messageContenu : String, type : String ) {
        try {
            modèle.envoyerMessage(
                destination = "/app/chat/${idConv}",
                contenu = messageContenu,
                type = type )
        } catch ( ex : SourceDeDonnéesException ) {
            Log.d("Exception : ", ex.message.toString())
        }
    }

}