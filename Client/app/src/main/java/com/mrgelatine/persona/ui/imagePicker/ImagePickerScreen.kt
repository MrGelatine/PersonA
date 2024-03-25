package com.mrgelatine.persona.ui.imagePicker

import android.app.Activity
import android.graphics.ImageDecoder.ImageInfo
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mrgelatine.persona.R
import com.mrgelatine.persona.ui.faceInfo.FaceInfoUI
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import com.mrgelatine.persona.ui.navigation.NavigationDestination

object ImagePickerDestination: NavigationDestination{
    override val route: String = "image_picker"
    override val titleRes: Int = R.string.image_picker_title
}

@Composable
fun ImagePickerScreen(
    navigateToImageInfo: () -> Unit,
    faceInfoViewModel: FaceInfoViewModel,
    activity: Activity
){
    val imagePickerDialog = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = {
            uri ->
            run {
                faceInfoViewModel.updateUI(FaceInfoUI(uri!!))
                faceInfoViewModel.sendFaceForFeatures(activity, uri!!)
                navigateToImageInfo()
            }
    })
    Button(onClick = {imagePickerDialog.launch("image/*")}) {
        
    }
}