package com.mrgelatine.persona.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.PersonAAPIViewModel
import com.mrgelatine.persona.ui.faceInfo.FaceInfoUI
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderViewModel
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesUI
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class PersonAAPIFaceInfoController(var faceInfoViewModel: FaceInfoViewModel) :
    Callback<FaceInfoResponse> {
    fun sendFace() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(PersonAAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val personAAPI: PersonAAPI = retrofit.create(PersonAAPI::class.java)
        val byteArrayOutputStream = ByteArrayOutputStream()
        faceInfoViewModel.faceInfoUI.value.faceData?.image?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val faceBase64: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        val call: Call<FaceInfoResponse> = personAAPI.loadFace(FaceInfoRequest(faceBase64))

        call.enqueue(this)
    }

    override fun onResponse(call: Call<FaceInfoResponse>, response: Response<FaceInfoResponse>) {
        if (response.isSuccessful) {
            val responseFields: FaceInfoResponse = response.body()!!
            faceInfoViewModel.updateUI(
                FaceInfoUI(FaceData(responseFields.faceFeatures,responseFields.rawEmbedding,
                    faceInfoViewModel.faceInfoUI.value.faceData?.image
                ), mapOf(), true, true)
            )
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<FaceInfoResponse?>?, t: Throwable) {
        Log.d("retrofit_post", "Error!")
        t.printStackTrace()
    }

}

class PersonAAPISimilarFaceController(var facesDataFlow: MutableStateFlow<List<FaceData>?>, val scope: CoroutineScope): Callback<SimilarFacesResponse> {
    fun sendFeatures(faceBias: FaceData, amount: Int) {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(PersonAAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val personAAPI: PersonAAPI = retrofit.create(PersonAAPI::class.java)
        val call: Call<SimilarFacesResponse> = personAAPI.findFaces(SimilarFacesRequest(faceBias.featureList!!, faceBias.rawEmbedding!!, amount))
        call.enqueue(this)
    }

    override fun onResponse(call: Call<SimilarFacesResponse>, response: Response<SimilarFacesResponse>) {
        if (response.isSuccessful) {
            val responseFields: SimilarFacesResponse = response.body()!!
            val similarFaces = responseFields.similarFaces
            val similarfacesData: MutableList<FaceData> = mutableListOf()
            for(face in similarFaces){
                val decodedString: ByteArray = Base64.decode(face.rawImage, Base64.DEFAULT)
                val decodedFace =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                similarfacesData.add(FaceData(face.faceFeatures,face.rawEmbedding, decodedFace))
            }
            Log.d("finish_similar_faces_retrofit", responseFields.similarFaces.size.toString())
            scope.launch {
                facesDataFlow.emit(similarfacesData.toList())
            }
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<SimilarFacesResponse>, t: Throwable) {
        Log.d("retrofit_post", "Error!")
        t.printStackTrace()
    }

}

class PersonAAPIRandomFacesController(var facesDataFlow: MutableStateFlow<List<FaceData>?>, val scope: CoroutineScope) :
    Callback<RandomFaceResponse> {
    fun getRandomFaces(amount: Int) {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(PersonAAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val personAAPI: PersonAAPI = retrofit.create(PersonAAPI::class.java)
        val call: Call<RandomFaceResponse> = personAAPI.getRandomFace(amount)

        call.enqueue(this)
    }

    override fun onResponse(call: Call<RandomFaceResponse>, response: Response<RandomFaceResponse>) {
        if (response.isSuccessful) {
            val responseFields: RandomFaceResponse = response.body()!!
            val decodedFaces: MutableList<FaceData> = mutableListOf()
            responseFields.randomFaces.forEach{
                val decodedString: ByteArray = Base64.decode(it.rawImage, Base64.DEFAULT)
                val decodedFace =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                decodedFaces.add(FaceData(it.faceFeatures, it.rawEmbedding, decodedFace))
            }
            this.scope.launch(Dispatchers.Main) {
                this@PersonAAPIRandomFacesController.facesDataFlow.emit(decodedFaces)
            }
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<RandomFaceResponse?>?, t: Throwable) {
        Log.d("retrofit_get", "Error!")
        t.printStackTrace()
    }

}