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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.ByteArrayOutputStream

class FaceInfoViewModel: ViewModel() {
    var free:Boolean = true
    val faceInfoUI: MutableState<FaceInfoUi> = mutableStateOf(FaceInfoUi())
    suspend fun sendFaceForFeatures(activity: Activity, choosedPhoto: Uri){
        if(free && faceInfoUI.value.featureList.isEmpty()){
            free = false
            val apiJob  = viewModelScope.async(Dispatchers.IO) {
                val personaAPIController = PersonAAPIController(faceInfoUI)
                val faceBitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, choosedPhoto)

                val byteArrayOutputStream = ByteArrayOutputStream()
                faceBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                personaAPIController.start(encoded)
                return@async true
            }
            free = apiJob.await()
        }

    }
}