package com.nicholson.nicmessenger.presentation.accueil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.accueil.ContratVuePrésentateurAccueil.*

class VueAccueil : Fragment(), IVueAccueil {
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurAccueil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_accueil, container, false)
    }

    override fun onViewCreated( vue: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        navController = Navigation.findNavController( vue )
        présentateur = PrésentateurAccueil( this )
        présentateur.traiterDémarrage()
    }

    override fun miseEnPlace() {
        présentateur.traiterObtenirConversation()
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueAccueil_vers_vueLogin )
    }
}
