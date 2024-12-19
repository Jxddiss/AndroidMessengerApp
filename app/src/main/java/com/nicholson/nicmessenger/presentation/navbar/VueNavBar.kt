package com.nicholson.nicmessenger.presentation.navbar

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.navbar.ContratVuePrésentateurNavBar.*

class VueNavBar : Fragment(), IVueNavBar {
    private lateinit var buttonMesDemandes : ImageButton
    private lateinit var buttonModifier : ImageButton
    private lateinit var floatingButtonHomeNav : FloatingActionButton
    private lateinit var buttonNotification : ImageButton
    private lateinit var buttonParametres : ImageButton
    private lateinit var indicateurNotifView : View
    private lateinit var présentateur : IPrésentateurNavBar
    private lateinit var navOptions : NavOptions
    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vue_nav_bar, container, false)
    }

    override fun onViewCreated( vue: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        buttonMesDemandes = vue.findViewById( R.id.buttonMesDemandes )
        buttonModifier = vue.findViewById( R.id.buttonModifier )
        floatingButtonHomeNav = vue.findViewById( R.id.floatingButtonHomeNav )
        buttonNotification = vue.findViewById( R.id.buttonNotification )
        buttonParametres = vue.findViewById( R.id.buttonParametres )
        indicateurNotifView = vue.findViewById( R.id.indicateurNotifView )
        navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById( R.id.fragmentContainerView ) as NavHostFragment?
        navController = navHostFragment?.navController
        présentateur = PréesentateurNavBar(this)
        présentateur.traiterDémarage()
    }

    override fun miseEnPlace() {
        navOptions = NavOptions.Builder()
            .setLaunchSingleTop( true )
            .setEnterAnim( com.google.android.material.R.anim.abc_fade_in )
            .setExitAnim( com.google.android.material.R.anim.abc_fade_out )
            .build()

        buttonMesDemandes.setOnClickListener { présentateur.traiterRedirigerÀDemandes() }
        buttonModifier.setOnClickListener { présentateur.traiterRedirigerÀProfile() }
        floatingButtonHomeNav.setOnClickListener { présentateur.traiterRedirigerÀAccueil() }
        buttonNotification.setOnClickListener { présentateur.traiterRedirigerÀNotification() }
        buttonParametres.setOnClickListener { présentateur.traiterRedirigerÀParametre() }
    }

    override fun redirigerÀDemandes() {
        navController?.navigate(
            resId = R.id.vueDemandes,
            args = null,
            navOptions = navOptions
        )
    }

    override fun redirigerÀProfile() {
        navController?.navigate(
            resId = R.id.vueProfile,
            args = null,
            navOptions = navOptions
        )
    }

    override fun redirigerÀAccueil() {
        navController?.navigate(
            resId = R.id.vueAccueil,
            args = null,
            navOptions = navOptions
        )
    }

    override fun redirigerÀNotification() {
        navController?.navigate(
            resId = R.id.vueNotifications,
            args = null,
            navOptions = navOptions
        )
    }

    override fun redirigerÀParametre() {
        navController?.navigate(
            resId = R.id.vueParametre,
            args = null,
            navOptions = navOptions
        )
    }

    override fun montrerNav() {
        view?.visibility = View.VISIBLE
    }

    override fun cacherNav() {
        view?.visibility = View.GONE
    }

    override fun montrerNotification() {
        indicateurNotifView.visibility = View.VISIBLE
        val mediaPlayer = MediaPlayer.create( requireContext(),R.raw.type )
        mediaPlayer.setVolume( 0.8f,0.8f )

        mediaPlayer.start()
    }
}