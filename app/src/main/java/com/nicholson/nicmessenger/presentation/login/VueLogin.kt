package com.nicholson.nicmessenger.presentation.login

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.login.ContratVuePrésentateurLogin.*

class VueLogin : Fragment(), IVueLogin {
    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var textInputLayoutEmail : TextInputLayout
    private lateinit var textInputLayoutPassword : TextInputLayout
    private lateinit var btnLogin : Button
    private lateinit var btnForgotPassword : Button
    private lateinit var présentateur : IPrésentateurLogin
    private lateinit var préférences : SharedPreferences
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val masterKey = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        préférences = EncryptedSharedPreferences.create(
            requireContext(),
            "auth_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_login, container, false)
    }

    override fun onViewCreated( vue: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        présentateur = PrésentateurLogin( this )
        emailEditText = vue.findViewById( R.id.emailEditText )
        textInputLayoutEmail = vue.findViewById( R.id.layoutEmailTextInput )
        textInputLayoutPassword = vue.findViewById( R.id.layoutPasswordTextInput )
        passwordEditText = vue.findViewById( R.id.passwordEditText )
        btnLogin = vue.findViewById( R.id.btnLogin )
        btnForgotPassword = vue.findViewById( R.id.btnForgotPassword )
        navController = Navigation.findNavController( vue )
        présentateur.traiterDémarage()
    }

    override fun miseEnPlace() {
        btnLogin.setOnClickListener{ présentateur.traiterSeConnecter() }
        btnForgotPassword.setOnClickListener { présentateur.traiterMotDePasseOublié() }
        emailEditText.addTextChangedListener {
            textInputLayoutEmail.error = null
        }
        passwordEditText.addTextChangedListener {
            textInputLayoutPassword.error = null
        }
    }

    override fun obtenirEmail(): String {
        return emailEditText.text.toString()
    }

    override fun obtenirMotDePasse(): String {
        return passwordEditText.text.toString()
    }

    override fun montrerMotDePasseInvalide() {
        textInputLayoutPassword.error = "Mot de passe invalide"
    }

    override fun montrerEmailInvalide() {
        textInputLayoutEmail.error = "Email invalide"
    }

    override fun redirigerÀAccueil() {
        navController.navigate( R.id.action_vueLogin_vers_vueAccueil )
    }

    override fun montrerErreurIdentifiants() {
        val dialogErreur = MaterialAlertDialogBuilder( requireContext() )
        dialogErreur.setMessage( getString( R.string.identifiant_invalide ) )
        dialogErreur.setPositiveButton( "OK" ) { dialog, _ ->
            dialog.dismiss()
        }
        dialogErreur.show()
    }

    override fun montrerErreurRéseau() {
        val dialogErreur = MaterialAlertDialogBuilder( requireContext() )
        dialogErreur.setMessage( getString( R.string.une_erreur_r_seau_c_est_produite ) )
        dialogErreur.setPositiveButton( "OK" ) { dialog, _ ->
            dialog.dismiss()
        }
        dialogErreur.show()
    }

    override fun obtenirTokenEnregistré(): String? {
        return préférences.getString( "token", null )
    }

    override fun retirerTokenEnregistré() {
        préférences.edit().remove( "token" ).apply()
    }

    override fun enregistrerTokenPréférences( token : String ) {
        préférences.edit().putString( "token", token ).apply()
    }

    override fun cacherEditTextPasswordEtBtnLogin() {
        btnLogin.visibility = View.GONE
        textInputLayoutPassword.visibility = View.GONE
    }

    override fun changerListenerMotDePasseOubliéVersConfirmerEnvEmail() {
        btnForgotPassword.setOnClickListener { présentateur.traiterMotDePasseOublié() }
    }

    override fun renitialiserListenerMotDePasseOublié() {
        btnForgotPassword.setOnClickListener { présentateur.traiterMotDePasseOublié() }
        btnLogin.visibility = View.VISIBLE
        textInputLayoutPassword.visibility = View.VISIBLE
    }

    override fun montrerMessageEmailMotDePasseOubliéEnvoyé() {
        Toast.makeText( requireContext(),
            getString( R.string.email_mot_de_passe_oublie ),
            Toast.LENGTH_SHORT ).show()
    }
}
