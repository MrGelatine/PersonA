package com.mrgelatine.persona.ui.personAFinder

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.mrgelatine.persona.R
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.navigation.NavigationDestination

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
    Row {
        Box {
            if (prePersonAFace != null) {
                prePersonAFace(
                    prePersonAFace = prePersonAFace,
                    restartCallback = { personaFinderViewModel.generateInitFaces(10) }
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
fun prePersonAFace(
    prePersonAFace: List<FaceData>?,
    restartCallback: () -> Unit
){
    Column {
        Row{
            prePersonAFace!![0].image?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "some useful description",
                    modifier = Modifier
                        .height(prePersonAFace!![0].image!!.height.dp)
                        .width(prePersonAFace!![0].image!!.width.dp)
                )
            }
        }
        Row{
            Button(
                onClick = restartCallback) {
                Text(text = "No")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Yes")
            }
        }
    }
}
@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun  SwipableFaces(facesForChoosing: MutableState<List<FaceData>?>,
                   swipeCardStates: List<SwipeableCardState>,
                   swipeCallback: (FaceData, Direction) -> Unit,
                   counterStatus: Pair<Int, Int?>,
                   modifier: Modifier){
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
