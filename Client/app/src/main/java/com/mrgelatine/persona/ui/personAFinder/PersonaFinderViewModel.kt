package com.mrgelatine.persona.ui.personAFinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.api.PersonAAPIRandomFaceController
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.ui.SimilarFaceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonaFinderViewModel : ViewModel(), SimilarFaceViewModel {
    var personaFinderUI: MutableStateFlow<PersonAFinderUI> = MutableStateFlow(PersonAFinderUI())
    fun changeNewPersona(){
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController: PersonAAPISimilarFaceController = PersonAAPISimilarFaceController(this@PersonaFinderViewModel)
            personaAPIController.getRandomFace()
            this@PersonaFinderViewModel
        }
    }
    override fun updateFaces(faces: List<FaceInfo>){
        viewModelScope.launch {
            personaFinderUI.emit(PersonAFinderUI(faces))
        }
    }

}