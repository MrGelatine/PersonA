package com.mrgelatine.persona.ui.similarFaces

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.mrgelatine.persona.R
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import com.mrgelatine.persona.ui.navigation.NavigationDestination


object SimilarFacesDestination: NavigationDestination {
    override val route: String = "similar_facces"
    override val titleRes: Int = R.string.image_picker_title
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimilarFacesScreen(
        navigateBack: () -> Unit,
        similarFacesViewModel: SimilarFacesViewModel,
        faceInfoViewModel: FaceInfoViewModel,
        navigateToFaceInfo: () -> Unit,
) {
    val similarFaces = similarFacesViewModel.similarFaces.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title={},
                navigationIcon = {IconButton(onClick = {navigateBack()}) {Icon(Icons.Filled.ArrowBack, contentDescription = "Menu") }})
        }
    ){
        innerPadding ->
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(innerPadding)) {
            items(similarFaces.value!!) { similarFace ->
                similarFace.image?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "some useful description",
                        modifier = Modifier.clickable {
                            faceInfoViewModel.updateFaceData(
                                FaceData(similarFace.featureList, similarFace.rawEmbedding, similarFace.image)
                            )
                            navigateToFaceInfo()
                        }
                    )
                }
            }

        }

    }
}
