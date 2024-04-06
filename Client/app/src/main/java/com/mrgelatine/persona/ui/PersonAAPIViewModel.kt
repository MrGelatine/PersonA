package com.mrgelatine.persona.ui

import com.mrgelatine.persona.api.FaceInfo

interface PersonAAPIViewModel {
    fun updateFaces(faces: List<FaceInfo>)
}