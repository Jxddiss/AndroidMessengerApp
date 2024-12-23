package com.nicholson.nicmessenger.presentation.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.nicholson.nicmessenger.R
import com.nicholson.nicmessenger.presentation.otd.UtilisateurOTD
import com.nicholson.nicmessenger.presentation.profile.ContratVuePrésentateurProfile.*
import java.io.File


class VueProfile : Fragment(), IVueProfile {

    companion object{
        private const val REQUEST_CODE_PICK_AVATAR = 0
    }

    private lateinit var layoutBarChargement : ConstraintLayout
    private lateinit var nomTextView : TextView
    private lateinit var descriptionTextView : TextView
    private lateinit var avatarImageView : ImageView
    private lateinit var banniereImageView : ImageView
    private lateinit var descriptionEditText : TextInputEditText
    private lateinit var statutDropdown : AutoCompleteTextView
    private lateinit var btnConfirmer : Button
    private lateinit var statutCardView : CardView
    private lateinit var bouttonDéconnexion : FloatingActionButton
    private lateinit var navController: NavController
    private lateinit var présentateur : IPrésentateurProfile
    private lateinit var statusCourrant : String
    private lateinit var statusMap : Map<String, String>
    private lateinit var displayStatus : List<String>
    private var avatarFile : File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vue_profile, container, false)
    }

    override fun onViewCreated( vue : View, savedInstanceState : Bundle? ) {
        super.onViewCreated( vue, savedInstanceState )
        layoutBarChargement = vue.findViewById( R.id.barDeChargement )
        nomTextView = vue.findViewById( R.id.nomTextView )
        descriptionTextView = vue.findViewById( R.id.descriptionTextView )
        avatarImageView = vue.findViewById( R.id.avatarImageView )
        banniereImageView = vue.findViewById( R.id.banniereImageView )
        statutCardView = vue.findViewById( R.id.statutCardView )
        bouttonDéconnexion = vue.findViewById( R.id.bouttonDéconnexion )
        descriptionEditText = vue.findViewById( R.id.descriptionEditText )
        statutDropdown = vue.findViewById( R.id.statutDropdown )
        btnConfirmer = vue.findViewById( R.id.btnConfirmer )
        navController = Navigation.findNavController( vue )
        présentateur = PrésentateurProfile( this )
        statusMap = mapOf(
            "online" to getString( R.string.online ),
            "disconnected" to getString( R.string.disconnected ),
            "inactif" to getString( R.string.inactif ),
            "absent" to getString( R.string.absent )
        )
        displayStatus = statusMap.values.toList()
        présentateur.traiterDémarrage()
    }

    override fun miseEnPlace() {
        bouttonDéconnexion.setOnClickListener {
            présentateur.traiterDeconnexion()
        }
        btnConfirmer.setOnClickListener {
            présentateur.mettreÀJourProfile()
        }

        val dropDownAdaptateur = ArrayAdapter(requireContext(), R.layout.statut_item, displayStatus)
        statutDropdown.setAdapter( dropDownAdaptateur )

        statutDropdown.keyListener = null

        statutDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedValue = statusMap.entries.firstOrNull { it.value == displayStatus[position] }?.key
            selectedValue?.let {
                statusCourrant = it
            }
        }

        avatarImageView.setOnClickListener { présentateur.traiterOuvrirGallerie() }
        présentateur.traiterObtenirUtilisateurConnecté()
    }

    override fun placerUtilisateur( utilisateurOTD: UtilisateurOTD ) {
        statusCourrant = utilisateurOTD.statut
        statutDropdown.setText( statusMap[statusCourrant], false )
        descriptionEditText.setText( utilisateurOTD.description )
        descriptionTextView.text = utilisateurOTD.description
        nomTextView.text = utilisateurOTD.nomComplet

        val avatar = utilisateurOTD.avatar
        if ( avatar.contains( "buddy2" ) ){
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
                getColorFromStatut( utilisateurOTD.statut, requireContext() ) ) )

        val bannière = utilisateurOTD.bannière
        if ( bannière.contains("default") ){
            banniereImageView.setImageResource( R.drawable.bann )
        } else {
            Glide.with( requireContext() )
                .load( bannière )
                .placeholder( R.drawable.bann )
                .error( R.drawable.bann  )
                .into( banniereImageView )
        }
    }

    override fun obtenirDescription(): String {
        return descriptionEditText.text.toString()
    }

    override fun obtenirStatus(): String {
        return statusCourrant
    }

    override fun montrerChargement() {
        layoutBarChargement.visibility = View.VISIBLE
    }

    override fun masquerChargement() {
        layoutBarChargement.visibility = View.GONE
    }

    override fun montrerErreurRéseau() {
        val dialogErreur = MaterialAlertDialogBuilder( requireContext() )
        dialogErreur.setMessage( getString( R.string.une_erreur_r_seau_c_est_produite ) )
        dialogErreur.setPositiveButton( "OK" ) { dialog, _ ->
            dialog.dismiss()
        }
        dialogErreur.show()
    }

    override fun montrerDialogSucces() {
        val dialogSucces = MaterialAlertDialogBuilder( requireContext() )
        dialogSucces.setMessage( getString( R.string.mis_a_jour_profile ) )
        dialogSucces.setPositiveButton( "OK" ) { dialog, _ ->
            dialog.dismiss()
        }
        dialogSucces.show()
    }

    override fun redirigerÀLogin() {
        navController.navigate( R.id.action_vueProfile_vers_vueLogin )
    }

    override fun ouvrirGalleriePhoto() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_AVATAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_AVATAR && resultCode == Activity.RESULT_OK) {
            data?.let {
                it.data?.let { uri ->
                    val inputStream = requireContext().contentResolver.openInputStream( uri )
                    val file = File(requireContext().cacheDir, "upload_image.jpg")
                    val outputStream = file.outputStream()
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()
                    avatarFile = file

                    Glide.with( requireContext() )
                        .load( uri )
                        .placeholder( R.drawable.buddy2 )
                        .error( R.drawable.buddy2 )
                        .into( avatarImageView )
                }
            }
        }
    }

    override fun obtenirNouvelAvatar() : File? {
        return avatarFile
    }

    private fun getColorFromStatut( statut : String, context : Context) : Int {
        return context.resources
            .getIdentifier(
                statut,
                "color",
                context.packageName )
    }
}
