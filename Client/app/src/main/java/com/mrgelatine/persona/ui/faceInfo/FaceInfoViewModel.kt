package com.mrgelatine.persona.ui.faceInfo

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.FaceDetector.Face
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrgelatine.persona.PersonAHiltApp
import com.mrgelatine.persona.api.PersonAAPIFaceInfoController
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.data.FaceDataEntity
import com.mrgelatine.persona.data.PersonARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class FaceInfoViewModel @Inject constructor(
    private val repository: PersonARepository
): ViewModel() {
    var faceData: MutableState<FaceData?> = mutableStateOf(null)

    fun sendFaceForFeatures(){
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController = PersonAAPIFaceInfoController(faceData, this@FaceInfoViewModel.viewModelScope, repository)
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
        updateFaceData(FaceData(null,null,faceBitmap))
    }
    fun loadFromPager(face: FaceDataEntity){
        updateFaceData(FaceData.fromFaceEntity(face))
    }
    fun updateFaceData(face: FaceData){
        viewModelScope.launch{
            faceData.value = face
        }
    }
}