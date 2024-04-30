package com.mrgelatine.persona.ui.personAFinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.SwipeableCardState
import com.mrgelatine.persona.api.PersonAAPIRandomFacesController
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.data.FaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonaFinderViewModel : ViewModel(){
    var faceForChoosing: MutableStateFlow<List<FaceData>?> = MutableStateFlow(
        listOf()
    )
    var prePersonAFace: MutableStateFlow<List<FaceData>?> = MutableStateFlow(
        null
    )
    var faceCardSwipeStates: MutableStateFlow<List<SwipeableCardState>> = MutableStateFlow(
        listOf()
    )
    var choosedFaces: MutableList<FaceData> = mutableListOf()
    var faceBias: FaceData = FaceData()
    var faceAmount: Int? = null
    var faceCounter: Int = 0

    lateinit var screenSize: Pair<Float,Float>
    fun addToBias(face: FaceData, direction: Direction){
        if(direction == Direction.Right){
            choosedFaces.add(face)
        }
        if(++faceCounter == faceAmount){
            val newFeatureList: MutableMap<String, Float> = mutableMapOf()
            choosedFaces.forEach{
                it.featureList!!.forEach {
                    if (it.key in newFeatureList.keys){
                        newFeatureList[it.key] = newFeatureList[it.key]!! + it.value
                        newFeatureList[it.key] = newFeatureList[it.key]!! / 2.0f

                    }else{
                        newFeatureList[it.key] = it.value
                    }
                }
            }
            faceBias.featureList = newFeatureList
            choosedFaces.clear()
            loadPrePersonA()
        }
    }

    fun loadPrePersonA(){
        val personAControlller = PersonAAPISimilarFaceController(this@PersonaFinderViewModel.prePersonAFace, viewModelScope)
        personAControlller.sendFeatures(faceBias, 1)
    }
    fun generateInitFaces(amount: Int){
        faceCounter = 0
        viewModelScope.launch(Dispatchers.IO) {
            prePersonAFace.emit(null)
            faceForChoosing.emit(null)
            val personaAPIController = PersonAAPIRandomFacesController(this@PersonaFinderViewModel.faceForChoosing, viewModelScope)
            this@PersonaFinderViewModel.faceAmount = amount
            personaAPIController.getRandomFaces(amount)
            this@PersonaFinderViewModel.faceCardSwipeStates.emit(List(amount){
                SwipeableCardState(screenSize.first, screenSize.second)
            })
        }
    }
}