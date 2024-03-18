package com.mrgelatine.persona.ui.faceInfo

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.ByteArrayOutputStream

class FaceInfoViewModel: ViewModel() {
    var free:Boolean = true
    val faceInfoUI: MutableState<FaceInfoUI> = mutableStateOf(FaceInfoUI())
    suspend fun sendFaceForFeatures(activity: Activity, choosedPhoto: Uri){
        if(free && faceInfoUI.value.featureList.isEmpty()){
            free = false
            val apiJob  = viewModelScope.async(Dispatchers.IO) {
                val personaAPIController = PersonAAPIFaceInfoController(faceInfoUI)
                var faceBitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, choosedPhoto)
                var imageWidth = faceBitmap.width
                var imageHeight = faceBitmap.height
                if(faceBitmap.width > 256){
                    imageWidth = 256
                }
                if (faceBitmap.height > 256){
                    imageHeight = 256
                }
                faceBitmap = Bitmap.createScaledBitmap(faceBitmap, imageWidth, imageHeight, false)
                val byteArrayOutputStream = ByteArrayOutputStream()
                faceBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                personaAPIController.sendFace(encoded)
                return@async true
            }
            free = apiJob.await()
        }

    }
}