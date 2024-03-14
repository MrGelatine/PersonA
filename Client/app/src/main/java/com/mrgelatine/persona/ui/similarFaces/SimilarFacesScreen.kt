package com.mrgelatine.persona.ui.similarFaces

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mrgelatine.persona.R
import com.mrgelatine.persona.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object SimilarFacesDestination: NavigationDestination {
    override val route: String = "similar_facces"
    override val titleRes: Int = R.string.image_picker_title
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimilarFacesScreen(
    navigateBack: () -> Unit,
    features: MutableState<Map<String, Float>>,
    amount: MutableState<Int>,
    modifier: Modifier
) {
    val viewModel:SimilarFacesViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        viewModel.sendFeatureForFaces(features.value, amount.value)
    }
    Scaffold(
        topBar = {
            TopAppBar(title={},
                navigationIcon = {IconButton(onClick = {navigateBack}) {Icon(Icons.Filled.ArrowBack, contentDescription = "Меню") }})
        }
    ){
        innerPadding ->
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(innerPadding)) {
            items(viewModel.similarFacesUI.value.similarFacesUI) { similarFace ->
                val decodedString: ByteArray = Base64.decode(similarFace, Base64.DEFAULT)
                val decodedFace =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                Image(
                    bitmap = decodedFace.asImageBitmap(),
                    contentDescription = "some useful description",
                )
            }

        }

    }
}
