package com.mrgelatine.persona.ui.faceInfo

import android.net.Uri

data class FaceInfoUi(
    var imageUri: Uri = Uri.EMPTY,
    var featureList: Map<String, Double> = mapOf(),
    var infoButtonEnabled: Boolean = false
)