package com.mrgelatine.persona.ui.similarFaces

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.ui.faceInfo.PersonAAPISimilarFaesController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SimilarFacesViewModel: ViewModel() {
    var free:Boolean = true
    val similarFacesUI: MutableStateFlow<SimilarFacesUI> = MutableStateFlow(SimilarFacesUI())
    suspend fun sendFeatureForFaces(features: Map<String, Float>, rawEmbedding:List<Float>, amount: Int){
        val vm = this
        if(free && similarFacesUI.value.similarFacesUI.isEmpty()){
            free = false
            val apiJob  = viewModelScope.async(Dispatchers.IO) {
                val personaAPIController = PersonAAPISimilarFaesController(vm)
                personaAPIController.sendFeatures(features, rawEmbedding, amount)
                return@async true
            }
            free = apiJob.await()
        }
    }
    fun changeUI(value:SimilarFacesUI){
        viewModelScope.launch {
            similarFacesUI.emit(value)
        }
    }
}