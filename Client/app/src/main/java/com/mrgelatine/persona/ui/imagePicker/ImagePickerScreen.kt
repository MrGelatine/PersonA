package com.mrgelatine.persona.ui.imagePicker

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalFoundationApi::class)
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
    Scaffold(
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    onClick = {imagePickerDialog.launch("image/*")}
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
                FloatingActionButton(
                    onClick = {
                        personAFinderViewModel.screenSize = Pair(screenWidth, screenHeight)
                        personAFinderViewModel.embeddingsSize = 9216
                        personAFinderViewModel.prepareFaces(10)
                        navigateToPersonaFinder()
                    }
                ) {
                    Icon(Icons.Default.Face, contentDescription = null)
                }
            }
        }
    ) {
        innerPadding ->
            Column(modifier= Modifier.padding(innerPadding)) {
                val pagerState = rememberPagerState(pageCount = {10})
                VerticalPager(state = pagerState) {

                }
            }

    }


}