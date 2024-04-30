package com.mrgelatine.persona.ui.similarFaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.data.FaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SimilarFacesViewModel: ViewModel() {
    val similarFaces: MutableStateFlow<List<FaceData>?> = MutableStateFlow(listOf())
    fun sendFeatureForFaces(features: Map<String, Float>, rawEmbedding:List<Float>, amount: Int){
        viewModelScope.launch(Dispatchers.IO) {
            similarFaces.emit(listOf())
            val personaAPIController = PersonAAPISimilarFaceController(similarFaces, this@SimilarFacesViewModel.viewModelScope)
            personaAPIController.sendFeatures(FaceData(features,rawEmbedding, null), amount)
        }
    }
}