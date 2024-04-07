package com.mrgelatine.persona.ui.imagePicker

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder.ImageInfo
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mrgelatine.persona.R
import com.mrgelatine.persona.data.FaceData
import com.mrgelatine.persona.ui.faceInfo.FaceInfoUI
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
    val imagePickerDialog = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = {
            uri ->
            run {
                var faceBitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, uri)
                var imageWidth = faceBitmap.width
                var imageHeight = faceBitmap.height
                if(faceBitmap.width > 256){
                    imageWidth = 256
                }
                if (faceBitmap.height > 256){
                    imageHeight = 256
                }
                faceBitmap = Bitmap.createScaledBitmap(faceBitmap, imageWidth, imageHeight, false)
                faceInfoViewModel.updateUI(FaceInfoUI(FaceData(null, null, faceBitmap), null))
                faceInfoViewModel.sendFaceForFeatures()
                navigateToImageInfo()
            }
    })
    Column {
        Button(onClick = {imagePickerDialog.launch("image/*")}) {

        }
        Button(onClick = {
            personAFinderViewModel.changeNewPersona()
            navigateToPersonaFinder()
        }
        ) {

        }
    }

}