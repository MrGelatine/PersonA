package com.mrgelatine.persona.ui.imagePicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrgelatine.persona.data.FaceDataEntity
import com.mrgelatine.persona.data.PersonARepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    val repository: PersonARepository
) : ViewModel(){
    var personAState: MutableStateFlow<PagingData<FaceDataEntity>> = MutableStateFlow(value = PagingData.empty())
    init{
        viewModelScope.launch (Dispatchers.IO){
            getMovies()
        }
    }
    private suspend fun getMovies() {
       repository.observePagingSource()
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                personAState.value = it
            }
    }
}