package com.mrgelatine.persona.ui.personAFinder

data class PersonAFinderUI(
    val rawImage: String? = null,
    val features: Map<String, Float>? = null,
    val rawEmbedding: List<Float>? = null
)