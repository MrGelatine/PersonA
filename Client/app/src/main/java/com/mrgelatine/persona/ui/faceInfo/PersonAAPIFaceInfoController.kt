package com.mrgelatine.persona.ui.faceInfo

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PersonAAPIFaceInfoController(var faceInfoUI: MutableState<FaceInfoUI>) : Callback<FaceInfoResponse> {
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
            Log.d("retrofit_post", responseFields.faceValue.toString())
            Log.d("retrofit_post", responseFields.rawEmbedding.toString())
            faceInfoUI.value = FaceInfoUI(faceInfoUI.value.imageUri, responseFields.faceValue, responseFields.rawEmbedding, true)
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<FaceInfoResponse?>?, t: Throwable) {
        Log.d("retrofit_post", "Error!")
        t.printStackTrace()
    }

}