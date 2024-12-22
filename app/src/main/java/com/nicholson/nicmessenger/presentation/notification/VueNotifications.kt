package com.nicholson.nicmessenger.presentation.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.notification.ContratVuePrésentateurNotifications.*
import com.nicholson.nicmessenger.presentation.otd.NotificationOTD

class VueNotifications : Fragment(), IVueNotifications {
    private lateinit var adapteur: RecyclerAdapterNotification
    private lateinit var recycler : RecyclerView
    private lateinit var layoutBarChargement : ConstraintLayout
    private lateinit var bouttonDéconnexion : FloatingActionButton
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurNotifications

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_notifications, container, false)
    }

    override fun onViewCreated( vue: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        layoutBarChargement = vue.findViewById( R.id.barDeChargement )
        recycler = vue.findViewById( R.id.recyclerNotifications )
        bouttonDéconnexion = vue.findViewById( R.id.bouttonDéconnexion )
        navController = Navigation.findNavController( vue )
        présentateur = PrésentateurNotifications( this )
        présentateur.traiterDémarrage()
    }

    override fun miseEnPlace() {
        bouttonDéconnexion.setOnClickListener {
            présentateur.traiterDeconnexion()
        }
        présentateur.traiterObtenirNotification()
    }

    override fun placerNotifications( notificationsOTD: List<NotificationOTD> ) {
        adapteur = RecyclerAdapterNotification( notificationsOTD.toMutableList() )
        recycler.layoutManager = LinearLayoutManager( requireContext() )
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.adapter = adapteur
        présentateur.attendreNotification()
        adapteur.listeInitialisé = true
    }

    override fun ajouterNotification( notificationOTD: NotificationOTD ) {
        adapteur.notificationsOTD.add( notificationOTD )
        adapteur.notifyItemInserted( adapteur.notificationsOTD.size - 1 )
        recycler.scrollToPosition( adapteur.notificationsOTD.size - 1 )
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueNotifications_vers_vueLogin )
    }

    override fun montrerChargement() {
        layoutBarChargement.visibility = View.VISIBLE
    }

    override fun masquerChargement() {
        layoutBarChargement.visibility = View.GONE
    }
}
