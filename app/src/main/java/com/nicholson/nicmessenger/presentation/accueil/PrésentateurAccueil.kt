package com.nicholson.nicmessenger.presentation.accueil

import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.accueil.ContratVuePrésentateurAccueil.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class PrésentateurAccueil( private val vue : IVueAccueil,
                           private val iocontext : CoroutineContext = Dispatchers.IO ) : IPrésentateurAccueil {

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
        vue.miseEnPlace()
    }

    override fun traiterObtenirConversation() {
        if( !modèle.estConnecté ) {
            vue.redirigerÀLogin()
        } else {

        }
    }


}