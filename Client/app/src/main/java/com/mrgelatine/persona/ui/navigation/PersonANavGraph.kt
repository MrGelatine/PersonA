package com.mrgelatine.persona.ui.navigation

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesDestination
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesScreen
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesUI
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PersonANavGraph(
    activity: Activity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    val imageUri = remember{ mutableStateOf(Uri.EMPTY) }
    val faceInfoViewModel: FaceInfoViewModel = viewModel()
    val similarFacesViewModel: SimilarFacesViewModel = viewModel()
    val amount = remember { mutableStateOf(0) }
    NavHost(
        navController = navController,
        startDestination = ImagePickerDestination.route,
        modifier = modifier
    ){
        composable(route = ImagePickerDestination.route){
            Log.d("image_picker_start", "")
            ImagePickerScreen(
                    navigateToImageInfo = {navController.navigate(FaceInfoDestination.route)},
                    activity = activity,
                    faceInfoViewModel= faceInfoViewModel
            )
        }
        composable(route = FaceInfoDestination.route){
            Log.d("face_info_start", "")
            FaceInfoScreen(
                    navigateToImagePicker = {navController.popBackStack()},
                    navigateToFaces = {navController.navigate(SimilarFacesDestination.route)},
                    faceInfoViewModel= faceInfoViewModel,
                    similarFacesViewModel = similarFacesViewModel
            )
        }
        composable(route = SimilarFacesDestination.route){

            SimilarFacesScreen(
                    navigateBack= {navController.popBackStack()},
                    similarFacesViewModel = similarFacesViewModel,
                    faceInfoViewModel= faceInfoViewModel,
                    navigateToFaceInfo = {navController.popBackStack()}
            )
        }
    }
}