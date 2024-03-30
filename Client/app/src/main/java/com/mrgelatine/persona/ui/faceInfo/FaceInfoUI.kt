package com.mrgelatine.persona.ui.faceInfo

import android.net.Uri

data class FaceInfoUI(
    var imageUri: Uri = Uri.EMPTY,
    var featureList: Map<String, Float> = mapOf(),
    var selectedFeatureList: Map<String, Float> = mapOf(),
    var rawEmbedding: List<Float> = listOf(),
    var infoButtonEnabled: Boolean = false,
    var rawImage: String = ""
)