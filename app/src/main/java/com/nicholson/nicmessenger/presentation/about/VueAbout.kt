package com.nicholson.nicmessenger.presentation.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.about.ContratVuePrésentateurAbout.*

class VueAbout : Fragment(), IVueAbout {
    private lateinit var bouttonDéconnexion : FloatingActionButton
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurAbout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_about, container, false)
    }

    override fun onViewCreated( vue : View, savedInstanceState : Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        bouttonDéconnexion = vue.findViewById( R.id.bouttonDéconnexion )
        navController = Navigation.findNavController( vue )
        présentateur = PrésentateurAbout( this )
        présentateur.traiterDémarrage()
    }

    override fun miseEnPlace() {
        bouttonDéconnexion.setOnClickListener {
            présentateur.traiterDeconnexion()
        }
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueAbout_vers_vueLogin )
    }
}
