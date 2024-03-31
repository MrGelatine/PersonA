package com.mrgelatine.persona.ui.faceInfo

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mrgelatine.persona.R
import com.mrgelatine.persona.ui.navigation.NavigationDestination
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesUI
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesViewModel
import kotlinx.coroutines.launch


object FaceInfoDestination: NavigationDestination{
    override val route: String = "face_info"
    override val titleRes: Int = R.string.image_picker_title
}
@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun FaceInfoScreen(
    navigateToImagePicker: () -> Unit,
    navigateToFaces: () -> Unit,
    faceInfoViewModel: FaceInfoViewModel,
    similarFacesViewModel: SimilarFacesViewModel

){
    val faceInfoUI by faceInfoViewModel.faceInfoUI.collectAsState()
    val featureToSearch = remember{ mutableStateOf(mutableMapOf<String, Float>()) }
    Column {
        if(faceInfoUI.imageUri != Uri.EMPTY){
            AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                            .data(faceInfoUI.imageUri)
                            .crossfade(true)
                            .build(),
                    contentDescription = stringResource(R.string.face_photo_description),
                    modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)
                            .align(alignment = Alignment.CenterHorizontally)
            )
        }else{
            val decodedString: ByteArray = Base64.decode(faceInfoUI.rawImage, Base64.DEFAULT)
            val decodedFace =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            Image(
                    bitmap = decodedFace.asImageBitmap(),
                    contentDescription = "some useful description",
                    modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)
                            .align(alignment = Alignment.CenterHorizontally)
            )
        }
        if(faceInfoUI.featureList.isEmpty()) {
            Row(modifier= Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .weight(1f)) {
                CircularProgressIndicator(
                    modifier = Modifier
                            .size(100.dp)
                            .align(alignment = Alignment.CenterVertically)
                )
            }
        } else {
            Column(modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                FeatureList(faceInfoUI, featureToSearch)
            }
        }
        Row(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = {
                    similarFacesViewModel.changeUI(SimilarFacesUI())
                    similarFacesViewModel.sendFeatureForFaces(featureToSearch.value,faceInfoUI.rawEmbedding, 10)
                    navigateToFaces()
                          },
                enabled = faceInfoUI.infoButtonEnabled,
                modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .align(alignment = Alignment.Bottom)
            ) {
                Text(text = "Load familiars")
            }

        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeatureList(
        faceInfoUI: FaceInfoUI,
        featureToSearch: MutableState<MutableMap<String, Float>>
){
    var longTapState by remember{ mutableStateOf(false) }
    for (elem in faceInfoUI.featureList) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .height(50.dp)
        ) {
            if(longTapState){
                var buttonState by remember{ mutableStateOf(false) }
                if(buttonState){
                    Button(
                            onClick = {
                                buttonState = !buttonState
                            },
                            modifier = Modifier
                                    .padding(start = 10.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .height(25.dp)
                                    .width(25.dp),
                            shape = CircleShape

                    ) {

                    }
                }else{
                    OutlinedButton(
                            onClick = {
                                featureToSearch.value[elem.key] = elem.value
                                buttonState = !buttonState
                            },
                            modifier = Modifier
                                    .padding(start = 10.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .height(25.dp)
                                    .width(25.dp),
                            shape = CircleShape

                    ) {

                    }
                }

            }else{
                featureToSearch.value = mutableMapOf()
            }

            Card(
                    elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                    ),
                    modifier = Modifier
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
                        text = elem.key + elem.value,
                        textAlign = TextAlign.Center
                )
            }
        }
    }
}

