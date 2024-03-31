package com.mrgelatine.persona.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.mrgelatine.persona.ui.faceInfo.FaceInfoUI
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderViewModel
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesUI
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonAAPIFaceInfoController(var faceInfoViewModel: FaceInfoViewModel) :
    Callback<FaceInfoResponse> {
    fun sendFace(faceBase64: String) {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(PersonAAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val personAAPI: PersonAAPI = retrofit.create(PersonAAPI::class.java)
        val call: Call<FaceInfoResponse> = personAAPI.loadFace(FaceInfoRequest(faceBase64))

        call.enqueue(this)
    }

    override fun onResponse(call: Call<FaceInfoResponse>, response: Response<FaceInfoResponse>) {
        if (response.isSuccessful) {
            val responseFields: FaceInfoResponse = response.body()!!
            faceInfoViewModel.updateUI(FaceInfoUI(faceInfoViewModel.faceInfoUI.value.imageUri, responseFields.faceFeatures, mapOf(), responseFields.rawEmbedding, true))
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<FaceInfoResponse?>?, t: Throwable) {
        Log.d("retrofit_post", "Error!")
        t.printStackTrace()
    }

}

class PersonAAPISimilarFaceController(val similarFacesUIViewModel: SimilarFacesViewModel): Callback<SimilarFacesResponse> {
    fun sendFeatures(faceEmbedding: Map<String, Float>, rawEmbedding:List<Float>, amount: Int) {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(PersonAAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val personAAPI: PersonAAPI = retrofit.create(PersonAAPI::class.java)
        val call: Call<SimilarFacesResponse> = personAAPI.findFaces(SimilarFacesRequest(faceEmbedding, rawEmbedding, amount))
        call.enqueue(this)
    }

    override fun onResponse(call: Call<SimilarFacesResponse>, response: Response<SimilarFacesResponse>) {
        if (response.isSuccessful) {
            val responseFields: SimilarFacesResponse = response.body()!!
            val similarFaces = responseFields.similarFaces
            Log.d("finish_similar_faces_retrofit", responseFields.similarFaces.size.toString())
            similarFacesUIViewModel.changeUI(SimilarFacesUI(similarFacesUI = responseFields.similarFaces))
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<SimilarFacesResponse>, t: Throwable) {
        Log.d("retrofit_post", "Error!")
        t.printStackTrace()
    }

}

class PersonAAPIRandomFaceController(var faceInfoViewModel: PersonaFinderViewModel) :
    Callback<RandomFaceResponse> {
    fun getRandomFace() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(PersonAAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val personAAPI: PersonAAPI = retrofit.create(PersonAAPI::class.java)
        val call: Call<RandomFaceResponse> = personAAPI.getRandomFace()

        call.enqueue(this)
    }

    override fun onResponse(call: Call<RandomFaceResponse>, response: Response<RandomFaceResponse>) {
        if (response.isSuccessful) {
            val responseFields: RandomFaceResponse = response.body()!!
            val randomFace = responseFields.radnomFace
            print(randomFace.faceFeatures)
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<RandomFaceResponse?>?, t: Throwable) {
        Log.d("retrofit_get", "Error!")
        t.printStackTrace()
    }

}