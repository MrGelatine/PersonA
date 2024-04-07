package com.mrgelatine.persona.ui.similarFaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.PersonAAPIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SimilarFacesViewModel: ViewModel(), PersonAAPIViewModel {
    val similarFacesUI: MutableStateFlow<SimilarFacesUI> = MutableStateFlow(SimilarFacesUI())
    fun sendFeatureForFaces(features: Map<String, Float>, rawEmbedding:List<Float>, amount: Int){
        val vm = this
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController = PersonAAPISimilarFaceController(vm)
            personaAPIController.sendFeatures(FaceData(features,rawEmbedding, null), amount)
        }
    }
    fun changeUI(value:SimilarFacesUI){

    }

    override fun updateFaces(faces: List<FaceData>) {
        viewModelScope.launch {
            similarFacesUI.emit(SimilarFacesUI(faces))
        }
    }
}