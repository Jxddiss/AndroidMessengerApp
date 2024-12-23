package com.nicholson.nicmessenger.presentation

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Demande
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IModèle {
    var estConnecté : Boolean
    var utilisateurConnecté : Utilisateur?
    var montrerNavUnit : (() -> Unit)?
    var cacherNavUnit : (() -> Unit)?
    var seDéconnecter : (() -> Unit)?
    var attendreNotificationNav : (() -> Unit)?
    var cacheIndicateurNotification : (() -> Unit)?
    var montrerIndicateurNotif : (() -> Unit)?
    var conversations : List<Conversation>
    var indiceConversationCourrante : Int
    var conversationCourrante : Conversation?
    var token : String?
    var currentStatus : String?
    var estSurVueNotifications : Boolean
    var attendNotif : Boolean
    var listeDemandes : List<Demande>
    var listeNotifications : List<Notification>
    var nomConversationCourrante : String
    var indicateurNotifVisible : Boolean
    var statutOnlineConnexionEnvoyé : Boolean
    fun cacherNav()
    fun seDéconnecter()
    fun mettreÀJourStatusAmi(status : String, position : Int )
    suspend fun seConnecter( email : String, motDePasse : String )
    suspend fun envoyerStatut()
    suspend fun demandeMotDePasseOublié( email : String )
    suspend fun obtenirMesConversations() : List<Conversation>
    suspend fun obtenirConversationCourrante() : Conversation
    suspend fun obtenirMessagesPrécédent() : List<Message>
    suspend fun subscribeMessage( topic : String ) : Flow<Message>
    suspend fun envoyerMessage( destination : String, contenu : String, type : String  )
    suspend fun envoyerNotificationMessage( notification : Notification, receveurId : Long )
    suspend fun subscribeStatus( topic : String ) : Flow<String>
    suspend fun obtenirNotificationsNonLu() : List<Notification>
    suspend fun subscribeNotifications() : Flow<Notification>
    suspend fun mettreNotificationLu( idNotifications : Long )
    suspend fun obtenirDemandes() : List<Demande>
    suspend fun accepterDemande( position : Int )
    suspend fun refuserDemande( position : Int )
    suspend fun mettreÀJourProfile( avatarFile : File?, bannièreFile : File? )
}