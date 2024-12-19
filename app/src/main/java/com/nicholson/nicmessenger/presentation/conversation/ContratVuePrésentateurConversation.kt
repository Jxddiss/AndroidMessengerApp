package com.nicholson.nicmessenger.presentation.conversation

import com.nicholson.nicmessenger.presentation.otd.ConversationOTD
import com.nicholson.nicmessenger.presentation.otd.MessageOTD

interface ContratVuePrésentateurConversation {
    interface IVueConversation {
        fun miseEnPlace()
        fun placerConversation( conversationOTD: ConversationOTD )
        fun placerMessagesPrécédents( messagesOTDS : List<MessageOTD> )
        fun ajouterMessage( messageOTD: MessageOTD )
        fun wizz()
        fun redirigerÀLogin()
        fun montrerChargement()
        fun masquerChargement()
        fun obtenirContenueMessage() : String
        fun mettreÀJourStatusAmi( status : String )
        fun obtenirDensitéÉcran() : Float
    }

    interface IPrésentateurConversation {
        fun traiterDémarrage()
        fun traiterObtenirConversation()
        fun traiterEnvoieMessage()
        fun traiterEnvoieWizz()
        fun attendreStatus()
    }
}