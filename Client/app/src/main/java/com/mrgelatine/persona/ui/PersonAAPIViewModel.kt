package com.mrgelatine.persona.ui

import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.data.FaceData

interface PersonAAPIViewModel {
    fun updateFaces(faces: List<FaceData>)
}