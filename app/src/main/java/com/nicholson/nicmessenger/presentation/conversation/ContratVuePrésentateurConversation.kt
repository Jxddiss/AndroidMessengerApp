package com.nicholson.nicmessenger.presentation.conversation

import com.nicholson.nicmessenger.presentation.otd.ConversationItemOTD
import com.nicholson.nicmessenger.presentation.otd.MessageOTD

interface ContratVuePrésentateurConversation {
    interface IVueConversation {
        fun miseEnPlace()
        fun placerConversation( conversationItemOTD: ConversationItemOTD, messages : List<MessageOTD> )
        fun ajouterMessage( messageOTD: MessageOTD )
    }

    interface IPrésentateurConversation {
        fun traiterDémarrage()
        fun traiterObtenirConversation()
    }
}