package com.mrgelatine.persona.ui.faceInfo

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.gson.GsonBuilder
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesUI
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonAAPISimilarFaesController(val similarFacesUIViewModel: SimilarFacesViewModel): Callback<SimilarFacesResponse> {
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
            Log.d("retrofit_post", responseFields.similarFaces.size.toString())
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