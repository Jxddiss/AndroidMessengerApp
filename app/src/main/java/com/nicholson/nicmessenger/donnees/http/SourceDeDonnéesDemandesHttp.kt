package com.nicholson.nicmessenger.donnees.http

import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.nicholson.nicmessenger.domaine.modele.Demande
import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesDemandes
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.donnees.jsonutils.GsonInstance
import okhttp3.MultipartBody
import okhttp3.Request
import okio.IOException

class SourceDeDonnéesDemandesHttp( val urlApi : String ) : ISourceDeDonnéesDemandes {
    override suspend fun obtenirDemandes( idUtilisateur: Long ): List<Demande> {
        val urlRequête = "$urlApi/demandes/${idUtilisateur}"

        val clientHttp = ClientHttp.obtenirInstance()
        val requête = Request.Builder()
            .url( urlRequête )
            .get()
            .build()

        try {
            val réponse = clientHttp.newCall( requête ).execute()
            if ( réponse.code == 200 ) {
                val corpsDeRéponse = réponse.body?.string()
                réponse.body?.close()
                if( corpsDeRéponse != null ){
                    val type = object : TypeToken<List<Demande>>() {}.type
                    return GsonInstance.obtenirInstance().fromJson( corpsDeRéponse, type )
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

    override suspend fun accepterDemande( idDemande: Long ) {
        val urlRequête = "$urlApi/demandes/accepter/${idDemande}"

        val clientHttp = ClientHttp.obtenirInstance()

        val corpsDeRequête = MultipartBody.Builder().setType( MultipartBody.FORM )
            .addFormDataPart( "id", idDemande.toString() )
            .build()

        val requête = Request.Builder()
            .url( urlRequête )
            .put( corpsDeRequête )
            .build()

        try {
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

    override suspend fun refuserDemande( idDemande: Long ) {
        val urlRequête = "$urlApi/demandes/refuser/${idDemande}"

        val clientHttp = ClientHttp.obtenirInstance()

        val corpsDeRequête = MultipartBody.Builder().setType( MultipartBody.FORM )
            .addFormDataPart( "id", idDemande.toString() )
            .build()

        val requête = Request.Builder()
            .url( urlRequête )
            .put( corpsDeRequête )
            .build()

        try {
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

}