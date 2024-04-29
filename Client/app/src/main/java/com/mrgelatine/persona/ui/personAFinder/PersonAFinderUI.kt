package com.mrgelatine.persona.ui.personAFinder

import com.alexstyl.swipeablecard.SwipeableCardState
import com.mrgelatine.persona.api.FaceInfo
import com.mrgelatine.persona.data.FaceData

data class PersonAFinderUI(
    val faces: List<Pair<FaceData, SwipeableCardState>> = listOf(),
    val isDetailed: Boolean = false
)