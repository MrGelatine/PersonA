package com.mrgelatine.persona.ui.faceInfo

import android.net.Uri

data class FaceInfoUI(
    var imageUri: Uri = Uri.EMPTY,
    var featureList: Map<String, Float> = mapOf(),
    var rawEmbedding: List<Float> = listOf(0.0f),
    var infoButtonEnabled: Boolean = false
)