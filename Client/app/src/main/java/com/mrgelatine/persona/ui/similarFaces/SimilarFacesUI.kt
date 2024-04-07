package com.mrgelatine.persona.ui.similarFaces

import android.net.Uri
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.data.FaceData

data class SimilarFacesUI(
    var similarFacesUI: List<FaceData> = listOf()
)