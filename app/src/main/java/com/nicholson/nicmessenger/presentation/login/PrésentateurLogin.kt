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
        val email = vue.obtenirEmailEnregistré()
        val motDePasse = vue.obtenirMotDePasseEnregistré()
        if ( email != null && motDePasse != null ){
            connexion( email, motDePasse )
        } else{
            vue.miseEnPlace()
        }
    }

    override fun traiterSeConnecter() {
        val email = vue.obtenirEmail()
        val motDePasse = vue.obtenirMotDePasse()

        if( email.isEmpty() && motDePasse.isEmpty() ){
            vue.montrerEmailInvalide()
            vue.montrerMotDePasseInvalide()
        } else if( email.isEmpty() ){
            vue.montrerEmailInvalide()
        } else if( motDePasse.isEmpty() ){
            vue.montrerMotDePasseInvalide()
        } else {
            connexion( email, motDePasse )
        }
    }

    override fun traiterMotDePasseOublié() {
        vue.cacherEditTextPasswordEtBtnLogin()
        vue.changerListenerMotDePasseOubliéVersConfirmerEnvEmail()
    }

    override fun traiterConfirmerMotDePasseOublié() {
        val email = vue.obtenirEmail()
        job = CoroutineScope( iocontext ).launch {
            try {
                modèle.demandeMotDePasseOublié( email )
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.montrerMessageEmailMotDePasseOubliéEnvoyé()
                    vue.renitialiserListenerMotDePasseOublié()
                }
            } catch ( ex : EmailInvalideException ){
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.montrerEmailInvalide()
                }
            } catch ( ex : SourceDeDonnéesException ) {
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.montrerErreurRéseau()
                }
            }
        }
    }

    override fun traiterAnnulerMotDePasseOublié() {
        vue.renitialiserListenerMotDePasseOublié()
    }

    private fun connexion( email : String, motDePasse : String ){
        vue.desactiverBouttons()
        job = CoroutineScope( iocontext ).launch {
            try {
                modèle.seConnecter( email, motDePasse )
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.enregistrerEmailPréférences( email )
                    vue.enregistrerMotDePassePréférence( motDePasse )
                    modèle.montrerNavUnit?.let { it() }
                    modèle.seDéconnecter = { seDéconnecter() }
                    vue.redirigerÀAccueil()
                }
            } catch ( ex : EmailInvalideException ){
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.reactiverBouttons()
                    vue.montrerEmailInvalide()
                }
            } catch ( ex : IdentifiantsException ) {
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.reactiverBouttons()
                    vue.montrerErreurIdentifiants()
                }
            } catch ( ex : SourceDeDonnéesException ) {
                CoroutineScope( Dispatchers.Main ).launch {
                    vue.reactiverBouttons()
                    vue.montrerErreurRéseau()
                }
            }
        }
    }

    private fun seDéconnecter() {
        modèle.cacherNav()
        modèle.estConnecté = false
        vue.retirerEmailEnregistré()
        vue.retirerMotDePasseEnregistré()
    }
}