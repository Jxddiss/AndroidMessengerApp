package com.nicholson.nicmessenger.presentation.accueil

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
import com.nicholson.nicmessenger.presentation.accueil.ContratVuePrésentateurAccueil.*
import com.nicholson.nicmessenger.presentation.otd.ConversationItemOTD

class VueAccueil : Fragment(), IVueAccueil {
    private lateinit var adaptateur : RecyclerAdapterConversation
    private lateinit var recyclerConversation : RecyclerView
    private lateinit var layoutBarChargement : ConstraintLayout
    private lateinit var bouttonDéconnexion : FloatingActionButton
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurAccueil
    private var conversations : MutableList<ConversationItemOTD> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_accueil, container, false)
    }

    override fun onViewCreated( vue: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        layoutBarChargement = vue.findViewById( R.id.barDeChargement )
        recyclerConversation = vue.findViewById( R.id.recyclerConversation )
        bouttonDéconnexion = vue.findViewById( R.id.bouttonDéconnexion )
        navController = Navigation.findNavController( vue )
        présentateur = PrésentateurAccueil( this )
        présentateur.traiterDémarrage()
    }

    override fun miseEnPlace() {
        bouttonDéconnexion.setOnClickListener {
            présentateur.traiterDeconnexion()
        }
        présentateur.traiterObtenirConversation()
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueAccueil_vers_vueLogin )
    }

    override fun redirigerÀConversation() {
        navController.navigate( R.id.action_vueAccueil_vers_vueConversation )
    }

    override fun montrerChargement() {
        layoutBarChargement.visibility = View.VISIBLE
    }

    override fun masquerChargement() {
        layoutBarChargement.visibility = View.GONE
    }

    override fun attacherListeConversationsRecycler(conversationsOTDS: List<ConversationItemOTD>) {
        conversations = conversationsOTDS.toMutableList()
        adaptateur = RecyclerAdapterConversation( conversations )
        adaptateur.itemCliquéÉvènement = {
            présentateur.traiterConversationCliquer( it )
        }
        recyclerConversation.layoutManager = LinearLayoutManager( requireContext() )
        recyclerConversation.itemAnimator = DefaultItemAnimator()
        recyclerConversation.adapter = adaptateur
        présentateur.attendreStatus()
    }

    override fun mettreÀJourStatus(position: Int, status : String ) {
        conversations[position].statut = status
        adaptateur.notifyItemChanged( position )
    }
}
