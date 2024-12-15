package com.nicholson.nicmessenger.presentation.login

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

    companion object {
        private val EMAIL_REGEX : Regex =
            Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|" +
                    "\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-" +
                    "\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                    "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|" +
                    "[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]" +
                    "*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\" +
                    "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
    }

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarage() {
        modèle = Modèle.obtenirInstance()
        vue.miseEnPlace()
    }

    override fun traiterSeConnecter() {
        val email = vue.obtenirEmail()
        val motDePasse = vue.obtenirMotDePasse()

        if( !validerEmail( email ) ) {
            vue.montrerEmailInvalide()
        } else if( motDePasse.isEmpty() ){
            vue.montrerMotDePasseInvalide()
        } else{
            job = CoroutineScope( iocontext ).launch {
                try {
                    modèle.seConnecter( email, motDePasse )
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.redirigerÀAccueil()
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

    private fun validerEmail( email : String ) : Boolean {
         return EMAIL_REGEX.matches( email )
    }
}