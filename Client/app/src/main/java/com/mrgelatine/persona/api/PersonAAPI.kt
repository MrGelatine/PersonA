package com.mrgelatine.persona.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class FaceInfoRequest(
    @SerializedName("faceBase64") val faceBase64: String
)

data class FaceInfoResponse(
    @SerializedName("face_features") val faceFeatures: Map<String,Float>,
    @SerializedName("raw_embedding") val rawEmbedding: List<Float>,
    @SerializedName("raw_image") val rawImage: String
)

data class SimilarFacesRequest(
    @SerializedName("face_features") val faceEmbedding: Map<String, Float>,
    @SerializedName("raw_embedding") val rawEmbedding: List<Float>,
    @SerializedName("amount") val amount: Int
)

data class SimilarFacesResponse(
    @SerializedName("similar_faces") val similarFaces: List<FaceInfo>
)
data class RandomFaceResponse(
    @SerializedName("random_faces") val randomFaces: List<FaceInfo>
)
data class FaceInfo(
    @SerializedName("face_features") val faceFeatures: Map<String, Float> = mapOf(),
    @SerializedName("raw_embedding") val rawEmbedding: List<Float> = listOf(),
    @SerializedName("raw_image") val rawImage: String = ""
)
interface PersonAAPI {
    @POST("faceInfo/")
    fun loadFace(@Body faceBase64: FaceInfoRequest): Call<FaceInfoResponse>

    @POST("similarFaces/")
    fun findFaces(@Body faceEmbedding: SimilarFacesRequest): Call<SimilarFacesResponse>

    @GET("randomFace/")
    fun getRandomFace(@Query("amount") amount: Int): Call<RandomFaceResponse>
    companion object {
        const val BASE_URL = "https://0e26-213-138-90-130.ngrok-free.app"
    }

}