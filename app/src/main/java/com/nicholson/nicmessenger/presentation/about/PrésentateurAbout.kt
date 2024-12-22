package com.nicholson.nicmessenger.presentation.about

import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.about.ContratVuePrésentateurAbout.*

class PrésentateurAbout( private val vue : IVueAbout ) : IPrésentateurAbout {
    private lateinit var modèle : IModèle
    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
        vue.miseEnPlace()
    }

    override fun traiterDeconnexion() {
        modèle.seDéconnecter()
        vue.redirigerÀLogin()
    }
}