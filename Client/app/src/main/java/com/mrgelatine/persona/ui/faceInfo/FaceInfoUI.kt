package com.mrgelatine.persona.ui.faceInfo

import android.net.Uri

data class FaceInfoUI(
    var imageUri: Uri = Uri.EMPTY,
    var featureList: Map<String, Float> = mapOf(),
    var infoButtonEnabled: Boolean = false
)