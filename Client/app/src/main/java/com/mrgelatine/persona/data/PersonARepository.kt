package com.mrgelatine.persona.data

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.util.UUID
import javax.inject.Inject

class PersonARepository @Inject constructor(
    private val faceDataDAO: FaceDataDAO,
    private val context: Context
){
    suspend fun getAll() = faceDataDAO.getAll()
    suspend fun insert(face: FaceDataEntity) = faceDataDAO.insert(face)
    fun saveImageToStorage(bitmap: Bitmap): String{
        val file = File(context.filesDir, UUID.randomUUID().toString()+ ".jpg")
        val savePath: String = file.path
        val outStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        return savePath
    }
    suspend fun insert(featureList: Map<String, Float>, rawEmbedding: List<Float>, image: String){
        val face: FaceDataEntity = FaceDataEntity(featureList= featureList, rawEmbedding= rawEmbedding, image= image)
        this.insert(face)
    }
}
