package com.nicholson.nicmessenger.presentation.login

import com.nicholson.nicmessenger.domaine.service.exceptions.EmailInvalideException
import com.nicholson.nicmessenger.donnees.exceptions.IdentifiantsException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.login.ContratVuePrésentateurLogin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurLogin(
    private val vue : IVueLogin,
    private val iocontext : CoroutineContext = Dispatchers.IO
) : IPrésentateurLogin {

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarage() {
        modèle = Modèle.obtenirInstance()
        vue.miseEnPlace()
    }

    override fun traiterSeConnecter() {
        val email = vue.obtenirEmail()
        val motDePasse = vue.obtenirMotDePasse()

        if( motDePasse.isEmpty() ){
            vue.montrerMotDePasseInvalide()
        } else {
            job = CoroutineScope( iocontext ).launch {
                try {
                    modèle.seConnecter( email, motDePasse )
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.redirigerÀAccueil()
                    }
                } catch ( ex : EmailInvalideException ){
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerEmailInvalide()
                    }
                } catch ( ex : IdentifiantsException ) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerErreurIdentifiants()
                    }
                } catch ( ex : SourceDeDonnéesException ) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerErreurRéseau()
                    }
                }
            }
        }
    }

    override fun traiterMotDePasseOublié() {
        vue.cacherEditTextPasswordEtBtnLogin()
        vue.changerListenerMotDePasseOubliéVersConfirmerEnvEmail()

    }

    override fun traiterConfirmerMotDePasseOublié() {
        vue.renitialiserListenerMotDePasseOublié()
    }
}