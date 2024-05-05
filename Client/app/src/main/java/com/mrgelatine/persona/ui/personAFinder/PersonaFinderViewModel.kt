package com.mrgelatine.persona.ui.personAFinder

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.SwipeableCardState
import com.mrgelatine.persona.api.PersonAAPIFaceParametrizeController
import com.mrgelatine.persona.api.PersonAAPIRandomFacesController
import com.mrgelatine.persona.api.PersonAAPISimilarFaceController
import com.mrgelatine.persona.data.FaceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PersonaFinderViewModel : ViewModel(){
    var faceForChoosing: MutableState<List<FaceData>?> = mutableStateOf(
        listOf()
    )
    var prePersonAFace: MutableState<List<FaceData>?> = mutableStateOf(
        null
    )
    var faceCardSwipeStates: MutableState<List<SwipeableCardState>> = mutableStateOf(
        listOf()
    )
    var choosedFaces: MutableList<FaceData> = mutableListOf()
    var emdeddingBias: List<Float> = listOf()
    var faceAmount: MutableState<Int?> = mutableStateOf(null)
    var faceCounter: MutableState<Int> = mutableStateOf(0)
    var embeddingsSize: Int = 0

    lateinit var screenSize: Pair<Float,Float>
    fun addToBias(face: FaceData, direction: Direction){
        if(direction == Direction.Right){
            choosedFaces.add(face)
        }
        if(++faceCounter.value == faceAmount.value){
            val newEmbeddingList: MutableList<Float> = MutableList(embeddingsSize){
                0.0f
            }
            choosedFaces.forEach{
                for(i in 0..<it.rawEmbedding!!.size){
                    newEmbeddingList[i] += it.rawEmbedding!![i]
                    newEmbeddingList[i] /= 2.0f
                }
            }
            emdeddingBias = newEmbeddingList
            choosedFaces.clear()
            loadPrePersonA()
        }
    }
    fun loadPrePersonA(){
        val personAControlller = PersonAAPISimilarFaceController(this@PersonaFinderViewModel.prePersonAFace)
        personAControlller.sendEmbedding(emdeddingBias, 1)
    }
    fun generateInitFaces(amount: Int){
        choosedFaces.clear()
        faceCounter.value = 0
        viewModelScope.launch(Dispatchers.IO) {
            prePersonAFace.value = null
            faceForChoosing.value = null
            val personaAPIController = PersonAAPIRandomFacesController(this@PersonaFinderViewModel.faceForChoosing)
            this@PersonaFinderViewModel.faceAmount.value = amount
            personaAPIController.getRandomFaces(amount)
            this@PersonaFinderViewModel.faceCardSwipeStates.value = List(amount){
                SwipeableCardState(screenSize.first, screenSize.second)
            }
        }
    }
}