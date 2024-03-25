package com.mrgelatine.persona.ui.faceInfo

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.PersonAAPIFaceInfoController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class FaceInfoViewModel: ViewModel() {
    var faceInfoUI: MutableStateFlow<FaceInfoUI> = MutableStateFlow(FaceInfoUI())
    fun sendFaceForFeatures(activity: Activity, imgUri:Uri){
        val vm = this
        if (imgUri != faceInfoUI.value.imageUri){
            viewModelScope.launch(Dispatchers.IO) {
                val personaAPIController = PersonAAPIFaceInfoController(vm)
                var faceBitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, faceInfoUI.value.imageUri)
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
            }
        }

    }
    fun updateUI(value:FaceInfoUI){
        viewModelScope.launch {
            faceInfoUI.emit(value)
        }
    }
}