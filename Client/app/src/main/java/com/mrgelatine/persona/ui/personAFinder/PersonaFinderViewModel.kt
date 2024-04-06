package com.mrgelatine.persona.ui.personAFinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrgelatine.persona.api.PersonAAPIRandomFaceController
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.ui.faceInfo.FaceInfoUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonaFinderViewModel : ViewModel(){
    var personaFinderUI: MutableStateFlow<PersonAFinderUI> = MutableStateFlow(PersonAFinderUI())
    fun changeNewPersona(){
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController: PersonAAPISimilarFaceController = PersonAAPISimilarFaceController(this@PersonaFinderViewModel)
            personaAPIController.getRandomFace()
            this@PersonaFinderViewModel
        }
    }
    fun updateUI(value: PersonAFinderUI){
        viewModelScope.launch {
            personaFinderUI.emit(value)
        }
    }
}