package com.mrgelatine.persona.ui.personAFinder

import com.alexstyl.swipeablecard.SwipeableCardState
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.data.FaceData

data class PersonAFinderUI(
    val faces: List<FaceData> = listOf(),
    val faceCardState: List<SwipeableCardState> = listOf()
)