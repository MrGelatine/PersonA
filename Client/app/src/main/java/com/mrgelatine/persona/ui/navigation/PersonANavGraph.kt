package com.mrgelatine.persona.ui.navigation

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mrgelatine.persona.ui.faceInfo.FaceInfoDestination
import com.mrgelatine.persona.ui.faceInfo.FaceInfoScreen
import com.mrgelatine.persona.ui.faceInfo.FaceInfoViewModel
import com.mrgelatine.persona.ui.imagePicker.ImagePickerDestination
import com.mrgelatine.persona.ui.imagePicker.ImagePickerScreen

@Composable
fun PersonANavGraph(
    activity: Activity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    val imageUri = remember{ mutableStateOf(Uri.EMPTY) }
    NavHost(
        navController = navController,
        startDestination = ImagePickerDestination.route,
        modifier = modifier
    ){
        composable(route = ImagePickerDestination.route){
            ImagePickerScreen(
                navigateToImageInfo = {navController.navigate(FaceInfoDestination.route)},
                choosedPhoto = imageUri,
                modifier = modifier
            )
        }
        composable(route = FaceInfoDestination.route){
            FaceInfoScreen(
                activity = activity,
                navigateBack = {navController.popBackStack()},
                choosedPhoto = imageUri,
                modifier = modifier
            )
        }
    }
}