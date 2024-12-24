package com.nicholson.nicmessenger.presentation.inscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.inscription.ContratVuePrésentateurInscription.*

class VueInscription : Fragment(), IVueInscription {
    private lateinit var nomEditText : TextInputEditText
    private lateinit var emailEditText : TextInputEditText
    private lateinit var passwordEditText : TextInputEditText
    private lateinit var confirmPasswordEditText : TextInputEditText
    private lateinit var textInputLayoutEmail : TextInputLayout
    private lateinit var textInputLayoutPassword : TextInputLayout
    private lateinit var textInputLayoutConfirmPassword : TextInputLayout
    private lateinit var textInputLayoutNom : TextInputLayout
    private lateinit var btnInscription : Button
    private lateinit var btnRetourLogin : Button
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurInscription

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_inscription, container, false)
    }

    override fun onViewCreated( vue : View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        nomEditText = vue.findViewById( R.id.nomEditText )
        emailEditText = vue.findViewById( R.id.emailEditText )
        passwordEditText = vue.findViewById( R.id.passwordEditText )
        confirmPasswordEditText = vue.findViewById( R.id.confirmPasswordEditText )
        textInputLayoutNom = vue.findViewById( R.id.layoutNomTextInput )
        textInputLayoutEmail = vue.findViewById( R.id.layoutEmailTextInput )
        textInputLayoutPassword = vue.findViewById( R.id.layoutPasswordTextInput )
        textInputLayoutConfirmPassword = vue.findViewById( R.id.layoutConfirmPasswordTextInput )
        btnInscription = vue.findViewById( R.id.btnInscription )
        btnRetourLogin = vue.findViewById( R.id.btnRetourLogin )
        navController = Navigation.findNavController( vue )
        présentateur = PrésentateurInscription( this )
        présentateur.traiterDémarage()
    }

    override fun miseEnPlace() {
        btnInscription.setOnClickListener { présentateur.traiterInscription() }
        btnRetourLogin.setOnClickListener { présentateur.traiterRedirigerLogin() }
        emailEditText.addTextChangedListener {
            textInputLayoutEmail.error = null
        }
        passwordEditText.addTextChangedListener {
            textInputLayoutPassword.error = null
        }
        nomEditText.addTextChangedListener {
            textInputLayoutNom.error = null
        }
        confirmPasswordEditText.addTextChangedListener {
            textInputLayoutConfirmPassword.error = null
        }
    }

    override fun obtenirEmail(): String = emailEditText.text.toString()

    override fun obtenirMotDePasse(): String = passwordEditText.text.toString()

    override fun obtenirNomComplet(): String = nomEditText.text.toString()

    override fun obtenirConfirmerMotDePasse(): String = confirmPasswordEditText.text.toString()

    override fun montrerMotDePasseInvalide() {
        textInputLayoutPassword.error = getString( R.string.short_mot_de_passe_invalide )
        montrerDialogErreur( getString( R.string.mot_de_passe_invalide ) )
    }

    override fun montrerMotDePasseNonIdentique() {
        textInputLayoutConfirmPassword.error = getString( R.string.confirm_pas_egal )
    }

    override fun montrerEmailInvalide() {
        textInputLayoutEmail.error = getString( R.string.email_invalide )
    }

    override fun montrerEmailExistant() {
        montrerDialogErreur( getString( R.string.email_exist ) )
        textInputLayoutEmail.error = getString( R.string.email_invalide )
    }

    override fun montrerNomInvalide() {
        textInputLayoutNom.error = getString( R.string.nom_vide )
    }

    override fun montrerErreurRéseau() {
        montrerDialogErreur( getString( R.string.une_erreur_r_seau_c_est_produite ) )
    }

    override fun montrerDialogSuccès() {
        val dialog = MaterialAlertDialogBuilder( requireContext() )
        dialog.setMessage( getString( R.string.inscription_reussi ) )
        dialog.setPositiveButton( "OK" ) { dialog, _ ->
            dialog.dismiss()
            présentateur.traiterRedirigerLogin()
        }
        dialog.show()
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueInscription_vers_vueLogin )
    }

    override fun redirigerÀAccueil() {
        navController.navigate( R.id.action_vueInscription_vers_vueAccueil )
    }

    override fun desactiverBoutton() {
        btnInscription.isClickable = false
    }

    override fun reactiverBoutton() {
        btnInscription.isClickable = true
    }

    private fun montrerDialogErreur( message : String ) {
        val dialogErreur = MaterialAlertDialogBuilder( requireContext() )
        dialogErreur.setMessage( message )
        dialogErreur.setPositiveButton( "OK" ) { dialog, _ ->
            dialog.dismiss()
        }
        dialogErreur.show()
    }
}