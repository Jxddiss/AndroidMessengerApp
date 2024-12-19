package com.nicholson.nicmessenger.presentation.conversation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.conversation.ContratVuePrésentateurConversation.*
import com.nicholson.nicmessenger.presentation.otd.ConversationOTD
import com.nicholson.nicmessenger.presentation.otd.MessageOTD

class VueConversation : Fragment(), IVueConversation {
    private lateinit var adapteur : RecyclerAdapterMessage
    private lateinit var recyclerMessages : RecyclerView
    private lateinit var layoutBarChargement : ConstraintLayout
    private lateinit var nomTextView : TextView
    private lateinit var statutTextView : TextView
    private lateinit var descriptionTextView : TextView
    private lateinit var avatarImageView : ImageView
    private lateinit var banniereImageView : ImageView
    private lateinit var messageEditText : TextInputEditText
    private lateinit var btnSend : ImageButton
    private lateinit var btnWizz : ImageButton
    private lateinit var statutCardView : CardView
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurConversation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_conversation, container, false)
    }

    override fun onViewCreated( vue: View, savedInstanceState: Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        layoutBarChargement = vue.findViewById( R.id.barDeChargement )
        recyclerMessages = vue.findViewById( R.id.recyclerMessage )
        nomTextView = vue.findViewById( R.id.nomTextView )
        statutTextView = vue.findViewById( R.id.statutTextView )
        descriptionTextView = vue.findViewById( R.id.descriptionTextView )
        avatarImageView = vue.findViewById( R.id.avatarImageView )
        banniereImageView = vue.findViewById( R.id.banniereImageView )
        messageEditText = vue.findViewById( R.id.messageEditText )
        btnSend = vue.findViewById( R.id.btnSend )
        btnWizz = vue.findViewById( R.id.btnWizz )
        statutCardView = vue.findViewById( R.id.statutCardView )
        présentateur = PrésentateurConversation( this )
        présentateur.traiterDémarrage()
    }

    override fun miseEnPlace() {
        btnSend.setOnClickListener { présentateur.traiterEnvoieMessage() }
        btnWizz.setOnClickListener { présentateur.traiterEnvoieWizz() }
        présentateur.traiterObtenirConversation()
    }

    override fun placerConversation( conversationOTD: ConversationOTD ) {
        nomTextView.text = conversationOTD.nomComplet
        statutTextView.text = getStatutStringFromStatut( conversationOTD.statut, requireContext() )
        descriptionTextView.text = conversationOTD.description
        val avatar = conversationOTD.avatar
        if ( avatar.contains("buddy2") ){
            avatarImageView.background =
                AppCompatResources.getDrawable( requireContext(), R.drawable.buddy2 )
        } else {
            Glide.with( requireContext() )
                .load( avatar )
                .error( R.drawable.buddy2  )
                .into( avatarImageView )
        }
        statutCardView.backgroundTintList =
            ColorStateList.valueOf( ContextCompat.getColor( requireContext(),
                getColorFromStatut( conversationOTD.statut, requireContext() ) ) )
        val bannière = conversationOTD.bannière
        if ( bannière.contains("default") ){
            banniereImageView.setImageResource( R.drawable.bann )
        } else {
            Glide.with( requireContext() )
                .load( bannière )
                .placeholder( R.drawable.bann )
                .error( R.drawable.bann  )
                .into( banniereImageView )
        }
        présentateur.attendreStatus()
    }

    override fun placerMessagesPrécédents( messagesOTDS: List<MessageOTD> ) {
        adapteur = RecyclerAdapterMessage( messagesOTDS.toMutableList() )
        recyclerMessages.layoutManager = LinearLayoutManager( requireContext() )
        recyclerMessages.itemAnimator = DefaultItemAnimator().apply {
            addDuration = 150
        }
        recyclerMessages.adapter = adapteur
        recyclerMessages.scrollToPosition( adapteur.messagesOTD.size - 1 )
    }

    override fun ajouterMessage(messageOTD: MessageOTD) {
        adapteur.messagesOTD.add( messageOTD )
        adapteur.notifyItemInserted( adapteur.messagesOTD.size -1 )
        recyclerMessages.scrollToPosition( adapteur.messagesOTD.size - 1 )
    }

    override fun wizz() {
        val shakeAnimationX = ObjectAnimator.ofFloat(view, "translationX", 0f, 30f, -30f, 30f, -30f, 0f)
        val shakeAnimationY = ObjectAnimator.ofFloat(view, "translationY", 0f, -20f, 20f, -20f, 20f, 0f)

        val shakeAnimatorSet = AnimatorSet().apply {
            playTogether( shakeAnimationX, shakeAnimationY )
        }
        shakeAnimatorSet.duration = 800
        shakeAnimatorSet.interpolator = CycleInterpolator(1f)

        context?.let {
            val mediaPlayer = MediaPlayer.create( it,R.raw.nudge )
            mediaPlayer.setVolume( 0.8f,0.8f )
            mediaPlayer.start()
        }

        shakeAnimatorSet.start()
        vibratePhone()
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueConversation_vers_vueLogin )
    }

    override fun montrerChargement() {
        layoutBarChargement.visibility = View.VISIBLE
    }

    override fun masquerChargement() {
        layoutBarChargement.visibility = View.GONE
    }

    override fun obtenirContenueMessage(): String {
        val contenu =  messageEditText.text.toString()
        messageEditText.text?.clear()
        return contenu
    }

    override fun mettreÀJourStatusAmi( status: String ) {
        context?.let {
            statutCardView.backgroundTintList =
                ColorStateList.valueOf( ContextCompat.getColor( it,
                    getColorFromStatut( status, it ) ) )
        }
    }

    override fun obtenirDensitéÉcran(): Float {
        return Resources.getSystem().displayMetrics.density
    }

    private fun getColorFromStatut( statut : String, context : Context) : Int {
        return context.resources
            .getIdentifier(
                statut,
                "color",
                context.packageName )
    }

    private fun getStatutStringFromStatut( statut: String, context : Context) : String {
        return context.getString(
            context.resources
                .getIdentifier(
                    statut,
                    "string",
                    context.packageName )
        )
    }

    private fun vibratePhone() {
        context?.let {
            val vibrator = it.getSystemService( Context.VIBRATOR_SERVICE ) as Vibrator

            if (vibrator.hasVibrator()) {
                val vibrationEffect = VibrationEffect.createOneShot(500,
                    VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            }
        }
    }
}
