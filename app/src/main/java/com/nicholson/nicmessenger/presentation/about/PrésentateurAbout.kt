package com.nicholson.nicmessenger.presentation.about

import android.util.Log
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.about.ContratVuePrésentateurAbout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurAbout( private val vue : IVueAbout,
                         private val iocontext : CoroutineContext = Dispatchers.IO )
    : IPrésentateurAbout {

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
        modèle.estSurVueNotifications = false
        modèle.nomConversationCourrante = ""
        vue.miseEnPlace()
    }

    override fun traiterDeconnexion() {
        job = CoroutineScope( iocontext ).launch {
            modèle.currentStatus = "disconnected"
            try {
                modèle.envoyerStatut()
            } catch ( ex : SourceDeDonnéesException) {
                Log.d( "Exception", "message : ${ex.message}" )
            }

            CoroutineScope( Dispatchers.Main ).launch {
                modèle.seDéconnecter()
                vue.redirigerÀLogin()
            }
        }
    }
}