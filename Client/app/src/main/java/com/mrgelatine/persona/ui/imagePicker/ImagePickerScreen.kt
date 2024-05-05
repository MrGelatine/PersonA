package com.mrgelatine.persona.ui.imagePicker

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mrgelatine.persona.R
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import com.mrgelatine.persona.ui.navigation.NavigationDestination
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderViewModel

object ImagePickerDestination: NavigationDestination{
    override val route: String = "image_picker"
    override val titleRes: Int = R.string.image_picker_title
}

@Composable
fun ImagePickerScreen(
    navigateToImageInfo: () -> Unit,
    navigateToPersonaFinder: () -> Unit,
    faceInfoViewModel: FaceInfoViewModel,
    personAFinderViewModel: PersonaFinderViewModel,
    activity: Activity
){
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val imagePickerDialog = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = {
            uri ->
            run {
                faceInfoViewModel.loadFaceImage(uri, activity.contentResolver)
                faceInfoViewModel.sendFaceForFeatures()
                navigateToImageInfo()
            }
    })
    Column {
        Button(onClick = {imagePickerDialog.launch("image/*")}) {
        }
        Button(onClick = {
            personAFinderViewModel.screenSize = Pair(screenWidth, screenHeight)
            personAFinderViewModel.embeddingsSize = 9216
            personAFinderViewModel.generateInitFaces(10)
            navigateToPersonaFinder()
        }
        ) {

        }
    }

}