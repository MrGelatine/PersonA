package com.mrgelatine.persona.ui.navigation

import android.app.Activity
import android.net.Uri
import android.util.Log
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
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderDestination
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderScreen
import com.mrgelatine.persona.ui.personAFinder.PersonaFinderViewModel
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesDestination
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesScreen
import com.mrgelatine.persona.ui.similarFaces.SimilarFacesViewModel

@Composable
fun PersonANavGraph(
    activity: Activity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    val imageUri = remember{ mutableStateOf(Uri.EMPTY) }
    val faceInfoViewModel: FaceInfoViewModel = viewModel()
    val similarFacesViewModel: SimilarFacesViewModel = viewModel()
    val personAFinderViewModel: PersonaFinderViewModel = viewModel()
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
                    navigateToPersonaFinder = {navController.navigate(PersonaFinderDestination.route)},
                    activity = activity,
                    faceInfoViewModel= faceInfoViewModel,
                    personAFinderViewModel = personAFinderViewModel
            )
        }
        composable(route = FaceInfoDestination.route){
            Log.d("face_info_start", "")
            FaceInfoScreen(
                    navigateToImagePicker = {navController.popBackStack()},
                    navigateToFaces = {navController.navigate(SimilarFacesDestination.route)},
                    navigateToPersonAFormation = {navController.navigate(PersonaFinderDestination.route)},
                    faceInfoViewModel= faceInfoViewModel,
                    similarFacesViewModel = similarFacesViewModel,
                    personAFinderViewModel = personAFinderViewModel
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
        composable(route = PersonaFinderDestination.route){
            PersonaFinderScreen(
                    personaFinderViewModel = personAFinderViewModel,
                    navigateBackToImagePicker = {navController.popBackStack()}
            )
        }
    }
}