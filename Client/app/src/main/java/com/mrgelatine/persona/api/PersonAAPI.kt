package com.mrgelatine.persona.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class FaceInfoRequest(
    @SerializedName("faceBase64") val faceBase64: String
)

data class FaceInfoResponse(
    @SerializedName("face") val faceValue: Map<String,Float>,
    @SerializedName("raw_embedding") val rawEmbedding: List<Float>,
)

data class SimilarFacesRequest(
    @SerializedName("faceFeatures") val faceEmbedding: Map<String, Float>,
    @SerializedName("rawEmbedding") val rawEmbedding: List<Float>,
    @SerializedName("amount") val amount: Int
)

data class SimilarFacesResponse(
    @SerializedName("similarFaces") val similarFaces: List<String>
)

interface PersonAAPI {
    @POST("faceInfo/")
    fun loadFace(@Body faceBase64: FaceInfoRequest): Call<FaceInfoResponse>

    @POST("similarFaces/")
    fun findFaces(@Body faceEmbedding: SimilarFacesRequest): Call<SimilarFacesResponse>

    companion object {
        const val BASE_URL = "https://92a9-213-138-90-130.ngrok-free.app"
    }

}