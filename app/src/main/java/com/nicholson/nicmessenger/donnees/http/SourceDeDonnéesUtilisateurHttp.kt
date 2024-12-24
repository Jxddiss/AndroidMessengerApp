package com.nicholson.nicmessenger.donnees.http

import android.util.Log
import android.webkit.MimeTypeMap
import com.google.gson.JsonSyntaxException
import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.EmailExistantException
import com.nicholson.nicmessenger.donnees.exceptions.IdentifiantsException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.donnees.jsonutils.GsonInstance
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.io.File

class SourceDeDonnéesUtilisateurHttp( val urlApi : String ) : ISourceDeDonéesUtilisateur {

    override suspend fun seConnecter( email: String, motDePasse: String ): Pair<String, Utilisateur> {
        val urlRequête = "$urlApi/login"

        val clientHttp = ClientHttp.obtenirInstance()

        try {

            val donnéesJson = GsonInstance.obtenirInstance().toJson(
                mapOf(
                    "email" to email,
                    "password" to motDePasse
                )
            )

            val corpsDeRequête = donnéesJson
                .toRequestBody("application/json".toMediaTypeOrNull())

            val requête = Request.Builder()
                .url( urlRequête )
                .post( corpsDeRequête )
                .build()

            val réponse = clientHttp.newCall( requête ).execute()
            if ( réponse.code == 200 ) {
                val corpsDeRéponse = réponse.body?.string()
                réponse.body?.close()
                if( corpsDeRéponse != null ){
                    val utilisateur = GsonInstance.obtenirInstance()
                        .fromJson( corpsDeRéponse, Utilisateur::class.java )

                    val token = réponse.headers["Jwt-Token"] ?: ""
                    return token to utilisateur
                } else {
                    throw SourceDeDonnéesException( "Corps de réponse vide" )
                }
            } else if( réponse.code == 400 || réponse.code == 401 || réponse.code == 403 ) {
                Log.d("identifiant", donnéesJson)
                Log.d("Exception", "Code : ${réponse.code}")
                réponse.body?.string()?.let { Log.d("Corps de réponse", it) }
                throw IdentifiantsException("Code : ${réponse.code}")
            } else {
                throw SourceDeDonnéesException("Code : ${réponse.code}")
            }
        } catch( ex : JsonSyntaxException) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        } catch( ex : IOException ) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        }
    }

    override suspend fun inscription( email: String, motDePasse: String, nomComplet: String ) {
        val urlRequête = "$urlApi/inscription"

        val clientHttp = ClientHttp.obtenirInstance()

        try {

            val corpsDeRequête = MultipartBody.Builder().setType( MultipartBody.FORM )
                .addFormDataPart( "email", email )
                .addFormDataPart( "nom", nomComplet )
                .addFormDataPart( "password", motDePasse )
                .build()

            val requête = Request.Builder()
                .url( urlRequête )
                .post( corpsDeRequête )
                .build()

            val réponse = clientHttp.newCall( requête ).execute()

            if ( réponse.code == 200 ) {
                réponse.body?.close()
            } else if( réponse.code == 400 || réponse.code == 401 || réponse.code == 403 ) {
                val corpsDeRéponse = réponse.body?.string()
                if ( corpsDeRéponse?.contains( "courriel" ) ?: false ){
                    throw EmailExistantException("Code : ${réponse.code}")
                }else{
                    throw SourceDeDonnéesException("Code : ${réponse.code}")
                }
            } else {
                throw SourceDeDonnéesException("Code : ${réponse.code}")
            }
        } catch( ex : JsonSyntaxException) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        } catch( ex : IOException ) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        }
    }

    override suspend fun demandeMotDePasseOublié( email: String ) {
        val urlRequête = "$urlApi/reset-password"

        val clientHttp = ClientHttp.obtenirInstance()

        try {

            val corpsDeRequête = MultipartBody.Builder().setType( MultipartBody.FORM )
                .addFormDataPart( "email", email )
                .build()

            val requête = Request.Builder()
                .url( urlRequête )
                .post( corpsDeRequête )
                .build()

            val réponse = clientHttp.newCall( requête ).execute()
            if ( réponse.code == 200 ) {
                réponse.body?.close()
            } else if( réponse.code == 403 || réponse.code == 401 ) {
                throw AuthentificationException("Code : ${réponse.code}")
            } else {
                throw SourceDeDonnéesException("Code : ${réponse.code}")
            }
        } catch( ex : JsonSyntaxException) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        } catch( ex : IOException ) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        }
    }

    override suspend fun mettreÀJourProfile(
        utilisateur: Utilisateur,
        avatarFile : File?,
        bannièreFile : File? ) : Utilisateur {

        val urlRequête = "$urlApi/utilisateur/update/${utilisateur.id}"

        val clientHttp = ClientHttp.obtenirInstance()

        try {

            val constructeurDeCorps = MultipartBody.Builder().setType( MultipartBody.FORM )
                .addFormDataPart( "nom", utilisateur.nomComplet )
                .addFormDataPart( "description", utilisateur.description )
                .addFormDataPart( "statut", utilisateur.statut )

            avatarFile?.let {
                constructeurDeCorps.apply {
                    addFormDataPart(
                        "avatar",
                        it.name,
                        it.asRequestBody(
                            getMimeTypeFromFile( it )?.toMediaTypeOrNull()
                        )
                    )
                }
            }

            bannièreFile?.let {
                constructeurDeCorps.apply {
                    addFormDataPart(
                        "banniere",
                        it.name,
                        it.asRequestBody(
                            getMimeTypeFromFile( it )?.toMediaTypeOrNull()
                        )
                    )
                }
            }

            val corpsDeRequête = constructeurDeCorps.build()

            val requête = Request.Builder()
                .url( urlRequête )
                .put( corpsDeRequête )
                .build()

            val réponse = clientHttp.newCall( requête ).execute()
            if ( réponse.code == 200 ) {
                val corpsDeRéponse = réponse.body?.string()
                réponse.body?.close()
                if( corpsDeRéponse != null ) {
                    val utilisateur = GsonInstance.obtenirInstance()
                        .fromJson(corpsDeRéponse, Utilisateur::class.java)
                    return utilisateur
                } else {
                    throw SourceDeDonnéesException( "Corps de réponse vide" )
                }
            } else if( réponse.code == 403 || réponse.code == 401 ) {
                throw AuthentificationException("Code : ${réponse.code}")
            } else {
                throw SourceDeDonnéesException("Code : ${réponse.code}")
            }
        } catch( ex : JsonSyntaxException) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        } catch( ex : IOException ) {
            throw SourceDeDonnéesException("Erreur inconnue : ${ex.message}")
        }
    }

    private fun getMimeTypeFromFile( file: File ): String? {
        val extension = file.extension
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension( extension.lowercase() )
    }
}