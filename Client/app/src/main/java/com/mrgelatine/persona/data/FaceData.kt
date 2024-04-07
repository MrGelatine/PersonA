package com.mrgelatine.persona.data

import android.graphics.Bitmap
import android.net.Uri

data class FaceData(
    var featureList: Map<String, Float>? = null,
    var rawEmbedding: List<Float>? = null,
    var image: Bitmap? = null
)