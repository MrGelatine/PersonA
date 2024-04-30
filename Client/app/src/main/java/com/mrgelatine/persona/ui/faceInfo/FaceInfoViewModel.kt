package com.mrgelatine.persona.ui.faceInfo

import android.app.Activity
import android.content.ContentResolver
import android.graphics.Bitmap
import android.media.FaceDetector.Face
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrgelatine.persona.api.PersonAAPIFaceInfoController
import com.mrgelatine.persona.data.FaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class FaceInfoViewModel: ViewModel() {
    var faceDataFlow: MutableStateFlow<FaceData?> = MutableStateFlow(null)
    fun sendFaceForFeatures(){
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController = PersonAAPIFaceInfoController(faceDataFlow, this@FaceInfoViewModel.viewModelScope)
            personaAPIController.sendFace()
        }
    }
    fun loadFaceImage(uri: Uri?, contentResolver: ContentResolver){
        var faceBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        var imageWidth = faceBitmap.width
        var imageHeight = faceBitmap.height
        if(faceBitmap.width > 256){
            imageWidth = 256
        }
        if (faceBitmap.height > 256){
            imageHeight = 256
        }
        faceBitmap = Bitmap.createScaledBitmap(faceBitmap, imageWidth, imageHeight, false)
        viewModelScope.launch {
            faceDataFlow.emit(FaceData(null,null,faceBitmap))
        }
    }
    fun updateFaceData(face: FaceData){
        viewModelScope.launch{
            faceDataFlow.emit(face)
        }
    }
}