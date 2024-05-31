package com.mrgelatine.persona.ui.personAFinder


import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.mrgelatine.persona.R
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.faceInfo.FaceFeaturePreviewed
import com.mrgelatine.persona.ui.navigation.NavigationDestination
import java.util.LinkedList

object PersonaFinderDestination: NavigationDestination{
    override val route: String = "persona_finder"
    override val titleRes: Int = R.string.persona_finder_title
}
@Composable
fun PersonaFinderScreen(
    navigateBackToImagePicker: () -> Unit,
    personaFinderViewModel: PersonaFinderViewModel
){
    val facesForChoosing = personaFinderViewModel.faceForChoosing
    val facesSwipeCards by personaFinderViewModel.faceCardSwipeStates
    val prePersonAFace by personaFinderViewModel.prePersonAFace

    val featureSelection = personaFinderViewModel.featureSelection
    val selectedTags = personaFinderViewModel.personATags
    val featureCollection = personaFinderViewModel.personAFeatures
    Row {
        Box {
            if (prePersonAFace != null) {
                PrePersonAFace(
                    prePersonAFace = prePersonAFace,
                    tagsSelection = featureSelection,
                    tagsCollection = selectedTags,
                    featureCollection = featureCollection,
                    restartCallback = { personaFinderViewModel.prepareFaces(10) }
                )
            } else {
                if(facesForChoosing.value != null){
                    SwipableFaces(facesForChoosing = facesForChoosing,
                        swipeCardStates = facesSwipeCards,
                        swipeCallback = { face, direction -> personaFinderViewModel.addToBias(face, direction)},
                        counterStatus = Pair(personaFinderViewModel.faceCounter.value, personaFinderViewModel.faceAmount.value),
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }else{
                    Row(modifier= Modifier
                        .align(alignment = Alignment.Center)
                    )
                    {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                                .align(alignment = Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun PrePersonAFace(
    prePersonAFace: List<FaceData>?,
    tagsSelection: MutableState<Boolean>,
    tagsCollection: MutableList<String>,
    featureCollection: MutableMap<String, Float>,
    restartCallback: () -> Unit
){
    var selection by tagsSelection
    val selectedTags = remember{ LinkedList<String>() }
    val face = remember{prePersonAFace?.get(0)}
    Column {
        Row{
            face?.image?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "some useful description",
                    modifier = Modifier
                        .height(face.image!!.height.dp)
                        .width(face.image!!.width.dp)
                )
            }
        }
        if(selection){
            Column {

                FaceFeaturePreviewed(faceData = prePersonAFace!![0], tagsToSearch = selectedTags, featureCollection= featureCollection, modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .weight(1f)
                    .padding(10.dp) )


                Button(onClick = {
                    featureCollection.putAll(face?.featureList!!)
                    tagsCollection.addAll(selectedTags)
                    restartCallback()
                                 },
                    modifier = Modifier.weight(1f)) {
                    Text(text = "Continue formation")
                }
            }
        }else{
            Row{
                Button(
                    onClick = restartCallback) {
                    Text(text = "No")
                }
                Button(onClick = { selection = !selection }) {
                    Text(text = "Yes")
                }
            }
        }

    }
}
@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun  SwipableFaces(
    facesForChoosing: MutableState<List<FaceData>?>,
    swipeCardStates: List<SwipeableCardState>,
    swipeCallback: (FaceData, Direction) -> Unit,
    counterStatus: Pair<Int, Int?>,
    modifier: Modifier
){
    val faces = facesForChoosing.value!!
    Column{
        Box{
            for (faceInd in 0..faces.size - 1) {
                val face = faces[faceInd]
                val state = swipeCardStates[faceInd]
                if (state.swipedDirection == null) {
                    Box(modifier = modifier
                        .swipableCard(
                            state = state,
                            blockedDirections = listOf(Direction.Down, Direction.Up),
                            onSwiped = { direction ->
                                swipeCallback(face, direction) },
                            onSwipeCancel = {
                                println("The swiping was cancelled")
                            }
                        )
                    ) {
                        Image(
                            bitmap = face.image!!.asImageBitmap(),
                            contentDescription = "some useful description",
                            modifier = Modifier
                                .height(face.image!!.height.dp)
                                .width(face.image!!.width.dp)
                        )
                    }
                }
            }
        }

        Row{
            Text(text = "${counterStatus.first + 1}/${counterStatus.second}")
        }
    }
}
