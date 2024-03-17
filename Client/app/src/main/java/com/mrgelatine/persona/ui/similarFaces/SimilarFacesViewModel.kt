package com.mrgelatine.persona.ui.similarFaces

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.ui.faceInfo.PersonAAPISimilarFaesController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class SimilarFacesViewModel: ViewModel() {
    var free:Boolean = true
    val similarFacesUI: MutableState<SimilarFacesUI> = mutableStateOf(SimilarFacesUI())
    suspend fun sendFeatureForFaces(features: Map<String, Float>, rawEmbedding:List<Float>, amount: Int){
        if(free && similarFacesUI.value.similarFacesUI.isEmpty()){
            free = false
            val apiJob  = viewModelScope.async(Dispatchers.IO) {
                val personaAPIController = PersonAAPISimilarFaesController(similarFacesUI)
                personaAPIController.sendFeatures(features, rawEmbedding, amount)
                return@async true
            }
            free = apiJob.await()
        }

    }
}