package com.nicholson.nicmessenger.presentation.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.otd.NotificationOTD

class RecyclerAdapterNotification( val notificationsOTD : MutableList<NotificationOTD> )
    :  RecyclerView.Adapter<RecyclerAdapterNotification.MyViewHolder>() {

    private var positionAnimés = mutableSetOf<Int>()
    var listeInitialisé = false

    class MyViewHolder( itemView : View) : RecyclerView.ViewHolder( itemView ) {
        val titreTextView : TextView = itemView.findViewById( R.id.titreTextView )
        val messageTextView : TextView = itemView.findViewById( R.id.descriptionTextView )
        val imageImageView : ImageView = itemView.findViewById( R.id.imageImageView )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView : View = LayoutInflater.from( parent.context )
            .inflate( R.layout.notification_item, parent, false )

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = notificationsOTD.size

    override fun onBindViewHolder( holder: MyViewHolder, position: Int ) {
        val image = notificationsOTD[position].image
        if ( !image.contains("buddy") ) {
            Glide.with( holder.itemView.context )
                .load( image )
                .placeholder( R.drawable.buddy2 )
                .error( R.drawable.buddy2  )
                .into( holder.imageImageView )
        }else{
            holder.imageImageView.setImageResource( R.drawable.buddy2 )
        }

        holder.titreTextView.text = notificationsOTD[position].titre
        holder.messageTextView.text = notificationsOTD[position].message

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