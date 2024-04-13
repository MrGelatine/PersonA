package com.mrgelatine.persona.ui.personAFinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.SwipeableCardState
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.PersonAAPIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonaFinderViewModel : ViewModel(), PersonAAPIViewModel {
    var personaFinderUI: MutableStateFlow<PersonAFinderUI> = MutableStateFlow(PersonAFinderUI())
    var choosedFaces: MutableList<FaceData> = mutableListOf()
    lateinit var faceBias: FaceData
    var faceAmount: Int = 10
    var faceCounter: Int = 0
    lateinit var screenSize: Pair<Float,Float>
    fun changeNewPersona(){
        faceCounter = 0
        viewModelScope.launch(Dispatchers.IO) {
            val personaAPIController: PersonAAPISimilarFaceController = PersonAAPISimilarFaceController(this@PersonaFinderViewModel)
            personaAPIController.sendFeatures(faceBias, faceAmount)
            this@PersonaFinderViewModel
        }
    }
    fun addToBias(face: FaceData, direction: Direction){
        if(direction == Direction.Right){
            choosedFaces.add(face)
        }
        if(++faceCounter == faceAmount){
            val newEmbedding: MutableList<Float> = MutableList(face.rawEmbedding!!.size){0.0f}
            val embeddingSize: Int = faceBias.rawEmbedding!!.size
            choosedFaces.forEach{
                for(embeddingInd in 0..embeddingSize-1) {
                    newEmbedding[embeddingInd] += face.rawEmbedding!![embeddingInd]
                    newEmbedding[embeddingInd] /= 2.0f
                }
            }
            faceBias.rawEmbedding = newEmbedding
            choosedFaces.clear()
            changeNewPersona()
        }
    }
    override fun updateFaces(faces: List<FaceData>){
        viewModelScope.launch {
            personaFinderUI.emit(PersonAFinderUI(faces, List(faces.size){SwipeableCardState(screenSize.first, screenSize.second)}))
        }
    }

}