package com.nicholson.nicmessenger.presentation.accueil

import com.nicholson.nicmessenger.presentation.otd.ConversationItemOTD

interface ContratVuePrésentateurAccueil {
    interface IVueAccueil{
        fun miseEnPlace()
        fun redirigerÀLogin()
        fun redirigerÀConversation()
        fun montrerChargement()
        fun masquerChargement()
        fun attacherListeConversationsRecycler( conversationsOTDS : List<ConversationItemOTD> )
    }

    interface IPrésentateurAccueil {
        fun traiterDémarrage()
        fun traiterObtenirConversation()
        fun traiterConversationCliquer( indice : Int )
    }
}