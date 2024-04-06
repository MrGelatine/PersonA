package com.mrgelatine.persona.ui.similarFaces

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.ui.SimilarFaceViewModel
import com.mrgelatine.persona.ui.faceInfo.FaceInfoUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class SimilarFacesViewModel: ViewModel(), SimilarFaceViewModel {
    val similarFacesUI: MutableStateFlow<SimilarFacesUI> = MutableStateFlow(SimilarFacesUI())
    fun sendFeatureForFaces(features: Map<String, Float>, rawEmbedding:List<Float>, amount: Int){
        val vm = this
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController = PersonAAPISimilarFaceController(vm)
            personaAPIController.sendFeatures(features, rawEmbedding, amount)
        }
    }
    fun changeUI(value:SimilarFacesUI){

    }

    override fun updateFaces(faces: List<FaceInfo>) {
        viewModelScope.launch {
            similarFacesUI.emit(SimilarFacesUI(faces))
        }
    }
}