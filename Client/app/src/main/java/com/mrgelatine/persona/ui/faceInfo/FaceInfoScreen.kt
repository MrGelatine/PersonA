package com.mrgelatine.persona.ui.faceInfo

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.shapes.Shape
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mrgelatine.persona.R
import com.mrgelatine.persona.ui.navigation.NavigationDestination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


object FaceInfoDestination: NavigationDestination{
    override val route: String = "face_info"
    override val titleRes: Int = R.string.image_picker_title
}
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FaceInfoScreen(
    activity: Activity,
    navigateBack: () -> Unit,
    choosedPhoto: MutableState<Uri>,
    modifier: Modifier
){
    val longTapState = remember{ mutableStateOf(false) }
    val featureToSearch = remember{ mutableStateOf(mutableMapOf(Pair("", 0.0))) }
    val coroutineScoop = rememberCoroutineScope()
    val viewModel:FaceInfoViewModel = viewModel()
    if(choosedPhoto.value != Uri.EMPTY) {
        viewModel.faceInfoUI.value.imageUri = choosedPhoto.value
    }
    coroutineScoop.launch{
        viewModel.sendFaceForFeatures(activity, choosedPhoto.value)
    }
    Column {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(viewModel.faceInfoUI.value.imageUri)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.face_photo_description),
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        if(viewModel.faceInfoUI.value.featureList.isEmpty()) {
            Row(modifier= Modifier.align(alignment = Alignment.CenterHorizontally).weight(1f)) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(alignment = Alignment.CenterVertically)
                )
            }
        } else
        {
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                for (elem in viewModel.faceInfoUI.value.featureList) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .height(50.dp)
                    ) {
                        if(longTapState.value){
                            var buttonState = remember{ mutableStateOf(false) }
                            if(buttonState.value){
                                Button(
                                    onClick = {
                                                buttonState.value = !buttonState.value
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
                                        buttonState.value = !buttonState.value
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
                            featureToSearch.value = mutableMapOf(Pair("", 0.0))
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
                                        longTapState.value = !longTapState.value
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
        }
        Row(modifier = Modifier.weight(0.5f)) {
            Button(
                onClick = { /*TODO*/ },
                enabled = viewModel.faceInfoUI.value.infoButtonEnabled,
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

