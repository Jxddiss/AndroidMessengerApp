package com.nicholson.nicmessenger.presentation.demande

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.otd.DemandeOTD

class RecyclerAdapterDemande( val demandesOTD : List<DemandeOTD>, val nomUtilisateurConnecté : String )
    : RecyclerView.Adapter<RecyclerAdapterDemande.MyViewHolder>(){

    var accepterDemandeClique: ((Int) ->Unit)? = null
    var refuserDemandeClique: ((Int) ->Unit)? = null
    private var positionAnimés = mutableSetOf<Int>()
    var listeInitialisé = false

    class MyViewHolder( itemView : View) : RecyclerView.ViewHolder( itemView ){
        val nomTextView : TextView = itemView.findViewById( R.id.nomTextView )
        val avatarImageView : ImageView = itemView.findViewById( R.id.avatarImageView )
        val btnRefuserDemande : Button = itemView.findViewById( R.id.btnRefuserDemande )
        val btnAccepterDemande : Button = itemView.findViewById( R.id.btnAccepterDemande )
        val statutTextView : TextView = itemView.findViewById( R.id.statutTextView )
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MyViewHolder {
        val itemView : View = LayoutInflater.from( parent.context )
            .inflate( R.layout.demande_item, parent, false )

        return MyViewHolder( itemView )
    }

    override fun getItemCount(): Int = demandesOTD.size

    override fun onBindViewHolder( holder: MyViewHolder, position: Int ) {
        val image = demandesOTD[position].image
        if ( !image.contains("buddy") ) {
            Glide.with( holder.itemView.context )
                .load( image )
                .placeholder( R.drawable.buddy2 )
                .error( R.drawable.buddy2  )
                .into( holder.avatarImageView )
        }else{
            holder.avatarImageView.setImageResource( R.drawable.buddy2 )
        }

        val nom = demandesOTD[position].nomComplet

        holder.nomTextView.text = nom

        if( nom == nomUtilisateurConnecté ) {
            holder.statutTextView.text = demandesOTD[position].statut
            holder.statutTextView.visibility = View.VISIBLE
            holder.btnAccepterDemande.visibility = View.GONE
            holder.btnAccepterDemande.isClickable = false
            holder.btnRefuserDemande.visibility = View.GONE
            holder.btnRefuserDemande.isClickable = false
        } else {
            holder.statutTextView.visibility = View.GONE
            holder.btnAccepterDemande.visibility = View.VISIBLE
            holder.btnAccepterDemande.isClickable = true
            holder.btnRefuserDemande.visibility = View.VISIBLE
            holder.btnRefuserDemande.isClickable = true
            holder.btnAccepterDemande.setOnClickListener { accepterDemandeClique?.invoke( position ) }
            holder.btnRefuserDemande.setOnClickListener { refuserDemandeClique?.invoke( position ) }
        }

        if ( !positionAnimés.contains( position ) ) {
            val animation = AnimationUtils.loadAnimation( holder.itemView.context,
                R.anim.glisser_de_la_gauche )

            if( !listeInitialisé ) {
                animation.startOffset = position * 100L
            }

            holder.itemView.startAnimation(animation)
            positionAnimés.add( position )
        }
    }
}