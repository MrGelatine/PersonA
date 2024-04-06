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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.mrgelatine.persona.R
import com.mrgelatine.persona.ui.navigation.NavigationDestination

object PersonaFinderDestination: NavigationDestination{
    override val route: String = "persona_finder"
    override val titleRes: Int = R.string.persona_finder_title
}
@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun PersonaFinderScreen(
    navigateBackToImagePicker: () -> Unit,
    personaFinderViewModel: PersonaFinderViewModel
){
    val state = rememberSwipeableCardState()
    val personaFinderUIState by personaFinderViewModel.personaFinderUI.collectAsState()
    Column {
        Box(modifier = Modifier.swipableCard(
            state = state,
            blockedDirections = listOf(Direction.Down,Direction.Up),
            onSwiped = { direction ->
                println("The card was swiped to $direction")
            },
            onSwipeCancel = {
                println("The swiping was cancelled")
            }
        )){
            personaFinderUIState.rawImage?.let {
                val decodedString: ByteArray = Base64.decode(personaFinderUIState.rawImage, Base64.DEFAULT)
                val decodedFace =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier= Modifier
                        .fillMaxSize()
                        .align(alignment = Alignment.Center)
                ) {
                    Image(
                        bitmap = decodedFace.asImageBitmap(),
                        contentDescription = "some useful description",
                        modifier = Modifier
                            .height(decodedFace.height.dp)
                            .width(decodedFace.width.dp)
                    )
                }
            }


        }
    }

}