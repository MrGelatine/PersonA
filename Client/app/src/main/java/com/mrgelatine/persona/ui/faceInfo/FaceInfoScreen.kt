package com.mrgelatine.persona.ui.faceInfo

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mrgelatine.persona.R
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.navigation.NavigationDestination
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderViewModel
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesViewModel
import java.util.LinkedList


object FaceInfoDestination: NavigationDestination{
    override val route: String = "face_info"
    override val titleRes: Int = R.string.image_picker_title
}
@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun FaceInfoScreen(
    navigateToImagePicker: () -> Unit,
    navigateToPersonAFormation: () -> Unit,
    navigateToFaces: () -> Unit,
    faceInfoViewModel: FaceInfoViewModel,
    similarFacesViewModel: SimilarFacesViewModel,
    personAFinderViewModel: PersonaFinderViewModel

){
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val faceData by faceInfoViewModel.faceData
    val tagsToSearch = remember{ LinkedList<String>() }

    Column {
        faceData?.image?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "some useful description",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
        FaceFeaturePreviewed(
            faceData = faceData!!,
            tagsToSearch = tagsToSearch,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .weight(1f)
                .padding(10.dp)
        )
        Row(modifier = Modifier.weight(0.25f)) {
            Button(
                onClick = {
                    similarFacesViewModel.sendFeatureForFaces(
                        faceData?.rawEmbedding!!, 10)
                    navigateToFaces()
                },
                enabled = !tagsToSearch.isEmpty() || faceData?.rawEmbedding != null,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Bottom)
            ) {
                Text(text = "Load familiars")
            }

        }
        Row(modifier = Modifier.weight(0.25f)) {
            Button(
                enabled = tagsToSearch.size > 0,
                onClick = {
                    personAFinderViewModel.personAFeatures.putAll(faceData!!.featureList!!)
                    personAFinderViewModel.screenSize = Pair(screenWidth, screenHeight)
                    personAFinderViewModel.embeddingsSize = 9216
                    personAFinderViewModel.prepareFaces(10)
                    navigateToPersonAFormation()
                },
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Bottom)
            )
            {
                Text(text = "Start PersonA Formation")
            }
        }
    }
}

@Composable
fun FaceFeaturePreviewed(
    tagsToSearch: LinkedList<String>,
    faceData: FaceData,
    featureCollection: MutableMap<String, Float>? = null,
    modifier: Modifier
){
    if(faceData.featureList != null && faceData.rawEmbedding != null) {
        Column(modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            FeatureList(faceData, tagsToSearch, featureCollection)
        }
    } else {
        Row(modifier = modifier){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeatureList(
    faceData: FaceData,
    tagsToSearch: LinkedList<String>,
    featureCollection: MutableMap<String, Float>?
){
    var longTapState by remember{ mutableStateOf(false) }
    val filteredFeatures = if (featureCollection?.entries != null) faceData.featureList?.filter { !featureCollection.containsKey(it.key) } else faceData.featureList
    faceData.tags?.forEach {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .height(50.dp)
        ) {
            var buttonState by remember{ mutableStateOf(false) }

            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                onClick = {
                    buttonState = !buttonState
                    if(buttonState){
                        tagsToSearch.add(it)
                    }else{
                        tagsToSearch.remove(it)
                    }
                },
                modifier = Modifier
                    .align(alignment = Alignment.CenterEnd)
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            longTapState = !longTapState
                        }
                    )
            ) {
                Text(
                    text = it,
                    textAlign = TextAlign.End,
                    modifier = Modifier.align(alignment = Alignment.End)
                )
            }
            if(buttonState){
                Checkbox(checked = true, onCheckedChange = {})
            }

        }
    }

}

