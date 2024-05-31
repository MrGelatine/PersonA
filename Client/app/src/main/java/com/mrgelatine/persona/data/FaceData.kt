package com.mrgelatine.persona.data

import android.R
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.File


data class FaceData(
    var featureList: Map<String, Float>? = mapOf(),
    var rawEmbedding: List<Float>? = listOf(),
    var image: Bitmap? = null,
    var tags: List<String>? = null
){
    companion object{
        fun fromFaceEntity(face: FaceDataEntity): FaceData{
            val imgFile = File(face.image)
            val image = BitmapFactory.decodeFile(imgFile.absolutePath)
            return FaceData(face.featureList, face.rawEmbedding, image, face.tags)
        }
    }
}

