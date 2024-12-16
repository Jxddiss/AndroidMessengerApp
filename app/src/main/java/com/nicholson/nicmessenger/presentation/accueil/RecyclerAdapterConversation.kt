package com.nicholson.nicmessenger.presentation.accueil

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.otd.ConversationItemOTD

class RecyclerAdapterConversation(
    val conversationsOTD : List<ConversationItemOTD>
) : RecyclerView.Adapter<RecyclerAdapterConversation.MyViewHolder>(){

    var itemCliquéÉvènement: ((Int) ->Unit)? = null
    private var positionAnimés = mutableSetOf<Int>()
    var listeInitialisé = false

    class MyViewHolder( itemView : View ) : RecyclerView.ViewHolder( itemView ) {
        val nomTextView : TextView = itemView.findViewById( R.id.nomTextView )
        val statutTextView : TextView = itemView.findViewById( R.id.statutTextView )
        val descriptionTextView : TextView = itemView.findViewById( R.id.descriptionTextView )
        val avatarImageView : ImageView = itemView.findViewById( R.id.avatarImageView )
        val statutCardView : CardView = itemView.findViewById( R.id.statutCardView )
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MyViewHolder {
        val itemView : View = LayoutInflater.from( parent.context )
            .inflate( R.layout.conversation_item, parent, false )

        return MyViewHolder( itemView )
    }

    override fun getItemCount(): Int = conversationsOTD.size

    override fun onBindViewHolder( holder: MyViewHolder, position: Int ) {
        holder.nomTextView.text = conversationsOTD[position].nomComplet
        holder.statutTextView.text =
            getStatutStringFromStatut( conversationsOTD[position].statut, holder.itemView.context )
        holder.descriptionTextView.text = conversationsOTD[position].description

        val avatar = conversationsOTD[position].avatar
        if ( !avatar.contains("buddy2") ){
            Glide.with( holder.itemView.context )
                .load( avatar )
                .placeholder( R.drawable.buddy2 )
                .error( R.drawable.buddy2  )
                .into( holder.avatarImageView )
        } else {
            holder.avatarImageView.setImageResource( R.drawable.buddy2 )
        }

        holder.statutCardView.backgroundTintList =
            ColorStateList.valueOf( ContextCompat.getColor(holder.itemView.context,
                getColorFromStatut( conversationsOTD[position].statut, holder.itemView.context ) ) )

        holder.itemView.setOnClickListener{
            itemCliquéÉvènement?.invoke(position)
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

    private fun getColorFromStatut( statut : String, context : Context ) : Int {
        return context.resources
            .getIdentifier(
                statut,
                "color",
                context.packageName )
    }

    private fun getStatutStringFromStatut( statut: String, context : Context ) : String {
        return context.getString(
            context.resources
                .getIdentifier(
                    statut,
                    "string",
                    context.packageName )
        )
    }

}