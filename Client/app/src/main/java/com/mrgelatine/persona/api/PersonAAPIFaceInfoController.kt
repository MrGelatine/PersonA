package com.mrgelatine.persona.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.mrgelatine.persona.ui.faceInfo.FaceInfoUI
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PersonAAPIFaceInfoController(var faceInfoViewModel: FaceInfoViewModel) : Callback<FaceInfoResponse> {
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