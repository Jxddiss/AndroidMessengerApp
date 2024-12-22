package com.nicholson.nicmessenger.presentation.demande

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.demande.ContratVuePrésentateurDemandes.*
import com.nicholson.nicmessenger.presentation.otd.DemandeOTD

class VueDemandes : Fragment(), IVueDemandes {
    private lateinit var adapteur: RecyclerAdapterDemande
    private lateinit var recycler : RecyclerView
    private lateinit var pasDeDemandesTextView : TextView
    private lateinit var layoutBarChargement : ConstraintLayout
    private lateinit var bouttonDéconnexion : FloatingActionButton
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurDemandes


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_demandes, container, false)
    }

    override fun onViewCreated( vue: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        layoutBarChargement = vue.findViewById( R.id.barDeChargement )
        recycler = vue.findViewById( R.id.recyclerDemandes )
        pasDeDemandesTextView = vue.findViewById( R.id.pasDeDemandesTextView )
        bouttonDéconnexion = vue.findViewById( R.id.bouttonDéconnexion )
        navController = Navigation.findNavController( vue )
        présentateur = PrésentateurDemandes( this )
        présentateur.traiterDémarrage()
    }

    override fun miseEnPlace() {
        bouttonDéconnexion.setOnClickListener {
            présentateur.traiterDeconnexion()
        }
        présentateur.traiterObtenirDemandes()
    }

    override fun placerDemandes( demandesOTD: List<DemandeOTD>, nomUtilisateurConnecté : String ) {
        if ( demandesOTD.isEmpty() ) {
            pasDeDemandesTextView.visibility = View.VISIBLE
        } else {
            pasDeDemandesTextView.visibility = View.GONE
        }

        adapteur = RecyclerAdapterDemande( demandesOTD, nomUtilisateurConnecté )
        recycler.layoutManager = LinearLayoutManager( requireContext() )
        recycler.itemAnimator = DefaultItemAnimator()
        adapteur.accepterDemandeClique = { présentateur.accepterDemande( it ) }
        adapteur.refuserDemandeClique = { présentateur.refuserDemande( it ) }
        recycler.adapter = adapteur
        adapteur.listeInitialisé = true
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueDemandes_vers_vueLogin )
    }

    override fun montrerChargement() {
        layoutBarChargement.visibility = View.VISIBLE
    }

    override fun masquerChargement() {
        layoutBarChargement.visibility = View.GONE
    }
}
