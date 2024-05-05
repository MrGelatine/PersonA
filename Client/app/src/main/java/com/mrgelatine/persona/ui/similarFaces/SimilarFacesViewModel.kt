package com.mrgelatine.persona.ui.similarFaces

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.data.FaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SimilarFacesViewModel: ViewModel() {
    val similarFaces: MutableState<List<FaceData>?> = mutableStateOf(listOf())
    fun sendFeatureForFaces(rawEmbedding:List<Float>, amount: Int){
        viewModelScope.launch(Dispatchers.IO) {
            similarFaces.value = listOf()
            val personaAPIController = PersonAAPISimilarFaceController(similarFaces)
            personaAPIController.sendEmbedding(rawEmbedding, amount)
        }
    }
}