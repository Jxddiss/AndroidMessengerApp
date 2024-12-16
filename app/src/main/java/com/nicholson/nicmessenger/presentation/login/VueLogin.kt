package com.nicholson.nicmessenger.presentation.login

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.login.ContratVuePrésentateurLogin.*

class VueLogin : Fragment(), IVueLogin {
    private lateinit var loginTitreTextView : TextView
    private lateinit var emailEditText : TextInputEditText
    private lateinit var passwordEditText : TextInputEditText
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
        loginTitreTextView = vue.findViewById( R.id.loginTitreTextView )
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

    override fun obtenirEmail(): String =
        emailEditText.text.toString()


    override fun obtenirMotDePasse(): String =
        passwordEditText.text.toString()


    override fun montrerMotDePasseInvalide() {
        textInputLayoutPassword.error = "Mot de passe invalide"
    }

    override fun montrerEmailInvalide() {
        textInputLayoutEmail.error = "Email invalide"
    }

    override fun redirigerÀAccueil() =
        navController.navigate( R.id.action_vueLogin_vers_vueAccueil )


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

    override fun obtenirEmailEnregistré(): String? =
        préférences.getString( "email", null )

    override fun obtenirMotDePasseEnregistré(): String? =
        préférences.getString( "motDePasse", null )

    override fun retirerEmailEnregistré() =
        préférences.edit().remove( "email" ).apply()

    override fun retirerMotDePasseEnregistré() =
        préférences.edit().remove( "motDePasse" ).apply()

    override fun enregistrerEmailPréférences(email: String) =
        préférences.edit().putString( "email", email ).apply()

    override fun enregistrerMotDePassePréférence(motDePasse: String) =
        préférences.edit().putString( "motDePasse", motDePasse ).apply()

    override fun cacherEditTextPasswordEtBtnLogin() {
        textInputLayoutPassword.visibility = View.GONE
        loginTitreTextView.text = getString( R.string.mot_de_passe_oubli )
    }

    override fun changerListenerMotDePasseOubliéVersConfirmerEnvEmail() {
        btnLogin.text = getString( R.string.confirmer )
        btnForgotPassword.text = getString( R.string.annuler )
        btnLogin.setOnClickListener { présentateur.traiterConfirmerMotDePasseOublié() }
        btnForgotPassword.setOnClickListener { présentateur.traiterAnnulerMotDePasseOublié() }
    }

    override fun renitialiserListenerMotDePasseOublié() {
        loginTitreTextView.text = getString( R.string.se_connecter )
        btnLogin.text = getString( R.string.se_connecter )
        btnForgotPassword.text = getString( R.string.mot_de_passe_oubli )
        textInputLayoutPassword.visibility = View.VISIBLE
        btnLogin.setOnClickListener { présentateur.traiterSeConnecter() }
        btnForgotPassword.setOnClickListener { présentateur.traiterMotDePasseOublié() }
    }

    override fun montrerMessageEmailMotDePasseOubliéEnvoyé() {
        val dialog = MaterialAlertDialogBuilder( requireContext() )
        dialog.setMessage( getString( R.string.email_mot_de_passe_oublie ) )
        dialog.setPositiveButton( "OK" ) { it, _ ->
            it.dismiss()
        }
        dialog.show()
    }
}
