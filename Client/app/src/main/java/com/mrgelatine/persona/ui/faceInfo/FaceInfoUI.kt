package com.mrgelatine.persona.ui.faceInfo

import android.net.Uri
import com.mrgelatine.persona.data.FaceData

data class FaceInfoUI(
    var faceData: FaceData?,
    var selectedFeatureList: Map<String, Float>?,
    var infoButtonEnabled: Boolean = false,
    var formationButtonEnabled: Boolean = false,
)