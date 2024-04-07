package com.mrgelatine.persona.ui.faceInfo

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.PersonAAPIFaceInfoController
import com.mrgelatine.persona.data.FaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class FaceInfoViewModel: ViewModel() {
    var faceInfoUI: MutableStateFlow<FaceInfoUI> = MutableStateFlow(FaceInfoUI(FaceData(),null))
    fun sendFaceForFeatures(){
        val vm = this
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController = PersonAAPIFaceInfoController(vm)
            personaAPIController.sendFace()
        }

    }
    fun updateUI(value:FaceInfoUI){
        viewModelScope.launch {
            faceInfoUI.emit(value)
        }
    }
}