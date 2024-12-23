package com.nicholson.nicmessenger.presentation.navbar

import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
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
    private lateinit var buttonAbout : ImageButton
    private lateinit var indicateurNotifView : View
    private lateinit var présentateur : IPrésentateurNavBar
    private lateinit var navOptions : NavOptions
    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null
    private var isAppInForeground = false

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
        buttonAbout = vue.findViewById( R.id.buttonAbout )
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
        buttonAbout.setOnClickListener { présentateur.traiterRedirigerÀAbout() }
        isAppInForeground = true
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

    override fun redirigerÀAbout() {
        navController?.navigate(
            resId = R.id.vueAbout,
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

        if ( isAppInForeground ) {
            val mediaPlayer = MediaPlayer.create( requireContext(),R.raw.type )
            mediaPlayer.setVolume( 0.5f,0.5f )

            mediaPlayer.start()
        }
    }

    override fun cacherNotification() {
        indicateurNotifView.visibility = View.GONE
    }

    override fun mettreBtnHomeGris() {
        floatingButtonHomeNav
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.grey ) )
        floatingButtonHomeNav.isClickable = false
    }

    override fun mettreBtnDemandeGris() {
        buttonMesDemandes
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.grey ) )
        buttonMesDemandes.isClickable = false
    }

    override fun mettreBtnProfileGris() {
        buttonModifier
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.grey ) )
        buttonModifier.isClickable = false
    }

    override fun mettreBtnNotificationsGris() {
        buttonNotification
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.grey ) )
        buttonNotification.isClickable = false
    }

    override fun mettreBtnAboutGris() {
        buttonAbout
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.grey ) )
        buttonAbout.isClickable = false
    }

    override fun mettreBtnHomeBlanc() {
        floatingButtonHomeNav
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.white ) )
        floatingButtonHomeNav.isClickable = true
    }

    override fun mettreBtnDemandeBlanc() {
        buttonMesDemandes
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.white ) )
        buttonMesDemandes.isClickable = true
    }

    override fun mettreBtnProfileBlanc() {
        buttonModifier
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.white ) )
        buttonModifier.isClickable = true
    }

    override fun mettreBtnNotificationsBlanc() {
        buttonNotification
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.white ) )
        buttonNotification.isClickable = true
    }

    override fun mettreBtnAboutBlanc() {
        buttonAbout
            .setColorFilter( ContextCompat.getColor( requireContext(), R.color.white ) )
        buttonAbout.isClickable = true
    }

    override fun onResume() {
        super.onResume()
        isAppInForeground = true
    }

    override fun onPause() {
        super.onPause()
        isAppInForeground = false
    }
}