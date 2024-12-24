package com.nicholson.nicmessenger.presentation.inscription

import com.nicholson.nicmessenger.domaine.service.exceptions.EmailInvalideException
import com.nicholson.nicmessenger.domaine.service.exceptions.MotDePasseInvalideException
import com.nicholson.nicmessenger.donnees.exceptions.EmailExistantException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.inscription.ContratVuePrésentateurInscription.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurInscription(
    private val vue : IVueInscription,
    private val iocontext : CoroutineContext = Dispatchers.IO
) : IPrésentateurInscription {

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarage() {
        modèle = Modèle.obtenirInstance()
        if ( modèle.estConnecté ) {
            vue.redirigerÀAccueil()
        } else {
            vue.miseEnPlace()
        }
    }

    override fun traiterInscription() {
        vue.desactiverBoutton()
        val email = vue.obtenirEmail()
        val motDePasse = vue.obtenirMotDePasse()
        val confirm = vue.obtenirConfirmerMotDePasse()
        val nomComplet = vue.obtenirNomComplet()

        if( nomComplet.isEmpty() ){
            vue.montrerNomInvalide()
            vue.reactiverBoutton()
            return
        }

        if( validerInputs( motDePasse, confirm ) ) {
            job = CoroutineScope( iocontext ).launch {
                try {
                    modèle.inscription( email, motDePasse, nomComplet )
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerDialogSuccès()
                    }
                } catch ( ex : EmailInvalideException){
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerEmailInvalide()
                        vue.reactiverBoutton()
                    }
                } catch ( ex : MotDePasseInvalideException ) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerMotDePasseInvalide()
                        vue.reactiverBoutton()
                    }
                }  catch ( ex : EmailExistantException ) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerEmailExistant()
                        vue.reactiverBoutton()
                    }
                } catch ( ex : SourceDeDonnéesException) {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.montrerErreurRéseau()
                        vue.reactiverBoutton()
                    }
                }
            }
        } else {
            vue.montrerMotDePasseNonIdentique()
            vue.reactiverBoutton()
        }
    }

    override fun traiterRedirigerLogin() {
        vue.redirigerÀLogin()
    }

    private fun validerInputs( motDePasse : String, confirm : String ) : Boolean {
        if ( motDePasse == confirm ) {
            return true
        }

        return false
    }
}