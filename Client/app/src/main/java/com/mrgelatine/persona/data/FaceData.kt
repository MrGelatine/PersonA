package com.mrgelatine.persona.data

import android.graphics.Bitmap
import android.net.Uri

data class FaceData(
    var featureList: Map<String, Float>? = mapOf(),
    var rawEmbedding: List<Float>? = listOf(),
    var image: Bitmap? = null
)