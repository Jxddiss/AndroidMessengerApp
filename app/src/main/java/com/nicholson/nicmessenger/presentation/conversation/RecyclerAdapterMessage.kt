package com.nicholson.nicmessenger.presentation.conversation

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.otd.MessageOTD

class RecyclerAdapterMessage(
    val messagesOTD : MutableList<MessageOTD>
) : RecyclerView.Adapter<RecyclerAdapterMessage.MyViewHolder>(){

    class MyViewHolder( itemView : View ) : RecyclerView.ViewHolder( itemView ) {
        val nomTextView : TextView = itemView.findViewById( R.id.nomTextView )
        val messageTextView : TextView = itemView.findViewById( R.id.messageTextView )
        val avatarImageView : ImageView = itemView.findViewById( R.id.avatarImageView )
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): MyViewHolder {
        val itemView : View = LayoutInflater.from( parent.context )
            .inflate( R.layout.message_item, parent, false )

        return MyViewHolder( itemView )
    }

    override fun getItemCount(): Int = messagesOTD.size

    override fun onBindViewHolder( holder: MyViewHolder, position: Int ) {
        holder.nomTextView.text = messagesOTD[position].nomSender
        holder.messageTextView.text = messagesOTD[position].contenu
        val avatar = messagesOTD[position].avatar
        if ( !avatar.contains("buddy2") ) {
            Glide.with( holder.itemView.context )
                .load( avatar )
                .placeholder( R.drawable.buddy2 )
                .error( R.drawable.buddy2  )
                .into( holder.avatarImageView )
        }  else {
            holder.avatarImageView.setImageResource( R.drawable.buddy2 )
        }
        messagesOTD[position].color?.let {
            try {
                holder.messageTextView.setTextColor( Color.parseColor( it ) )
            } catch ( ex : IllegalArgumentException ) {
                Log.d( "Color parse", "Cant parse color" )
            }
        }
    }

}