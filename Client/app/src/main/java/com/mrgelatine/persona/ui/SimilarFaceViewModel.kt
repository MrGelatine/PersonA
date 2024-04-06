package com.mrgelatine.persona.ui

import com.mrgelatine.persona.api.FaceInfo

interface SimilarFaceViewModel {
    fun updateFaces(faces: List<FaceInfo>)
}