package com.mrgelatine.persona.ui.imagePicker

import androidx.lifecycle.ViewModel
import com.mrgelatine.persona.data.PersonARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    val repository: PersonARepository
): ViewModel(){

}