package com.mrgelatine.persona.ui.imagePicker

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mrgelatine.persona.R
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.data.FaceDataEntity
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import com.mrgelatine.persona.ui.navigation.NavigationDestination
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderViewModel
import java.util.Date


object ImagePickerDestination: NavigationDestination{
    override val route: String = "image_picker"
    override val titleRes: Int = R.string.image_picker_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerScreen(
    navigateToImageInfo: () -> Unit,
    navigateToPersonaFinder: () -> Unit,
    imagePickerViewModel: ImagePickerViewModel,
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
        }
    )
    val photoMakerDialog = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            bitmap ->
            run{
                faceInfoViewModel.updateFaceData(FaceData(null, null, bitmap!!))
                faceInfoViewModel.sendFaceForFeatures()
                navigateToImageInfo()
            }
        }
    )
    val permissionDialog = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
        onResult = {
                result ->
            run{
                if(result){
                    photoMakerDialog.launch()
                }
            }
        }
    )
    val personAPaging: LazyPagingItems<FaceDataEntity> = imagePickerViewModel.personAState.collectAsLazyPagingItems()
    Scaffold(
        floatingActionButton = {
            Row(modifier= Modifier.wrapContentHeight()) {
                FloatingActionButton(
                    modifier= Modifier.wrapContentSize().padding(end = 10.dp),
                    shape = RoundedCornerShape(30),
                    onClick = {imagePickerDialog.launch("image/*")},
                ) {

                    Icon(
                        imageVector= Icons.Default.Search,
                        contentDescription = null,
                    )
                }
                FloatingActionButton(
                    modifier= Modifier.wrapContentSize().padding(end = 10.dp),
                    shape = RoundedCornerShape(30),
                    onClick = {
                        personAFinderViewModel.screenSize = Pair(screenWidth, screenHeight)
                        personAFinderViewModel.embeddingsSize = 9216
                        personAFinderViewModel.prepareFaces(10)
                        navigateToPersonaFinder()
                    }
                ) {
                    Icon(Icons.Default.Face, contentDescription = null)
                }
                FloatingActionButton(
                    modifier= Modifier.wrapContentSize().padding(end = 10.dp),
                    shape = RoundedCornerShape(30),
                    onClick = {
                        permissionDialog.launch(Manifest.permission.CAMERA)
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null
                    )
                }
            }
        }
    ) {
        innerPadding ->
            Column(modifier= Modifier.padding(innerPadding)) {
                LazyVerticalGrid(
                    columns= GridCells.Adaptive(100.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 10.dp)
                ) {
                    items(personAPaging.itemCount){index ->
                        PersonAInfoHistoryLine(face = personAPaging[index]!!
                        ) {
                            faceInfoViewModel.loadFromPager(personAPaging[index]!!)
                            navigateToImageInfo()
                        }
                    }
                }
            }

    }
}
@Composable
fun PersonAInfoHistoryLine(face: FaceDataEntity,clickCallback: () -> Unit){
    Row(modifier= Modifier
        .clickable (
            onClick = {clickCallback()}
        )
    ){
        Card(shape= RoundedCornerShape(10),
            modifier= Modifier.padding(5.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(face.image)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .build(),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(5.dp),
                contentDescription = "Face Picture"
            )

            val timePassed = FaceDataEntity.duration(face.added, Date(System.currentTimeMillis()))
            var text:String
            if (timePassed["years"]!! > 0 || timePassed["months"]!! > 0) {
                text = face.added.toString()
            } else if (timePassed["days"]!! > 0) {
                text = "${timePassed["days"]!!} days ago"
            } else if (timePassed["hours"]!! > 0) {
                text = "${timePassed["hours"]!!} hours ago"
            } else if (timePassed["minutes"]!! > 0) {
                text = "${timePassed["minutes"]!!} minutes ago"
            } else if (timePassed["seconds"]!! > 10) {
                text = "${timePassed["seconds"]!!} seconds ago"
            } else {
                text = "a second ago"
            }
            Text(
                text=text,
                modifier= Modifier.padding(5.dp)
            )
        }

    }
}