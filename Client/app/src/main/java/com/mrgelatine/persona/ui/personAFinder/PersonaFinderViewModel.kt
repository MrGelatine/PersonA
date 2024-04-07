package com.mrgelatine.persona.ui.personAFinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.PersonAAPIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonaFinderViewModel : ViewModel(), PersonAAPIViewModel {
    var personaFinderUI: MutableStateFlow<PersonAFinderUI> = MutableStateFlow(PersonAFinderUI())
    lateinit var faceBias: FaceData
    var faceAmount: Int = 5
    var faceCounter: Int = 1
    fun changeNewPersona(){
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController: PersonAAPISimilarFaceController = PersonAAPISimilarFaceController(this@PersonaFinderViewModel)
            personaAPIController.sendFeatures(faceBias, faceAmount)
            this@PersonaFinderViewModel
        }
    }
    override fun updateFaces(faces: List<FaceData>){
        viewModelScope.launch {
            personaFinderUI.emit(PersonAFinderUI(faces))
        }
    }

}