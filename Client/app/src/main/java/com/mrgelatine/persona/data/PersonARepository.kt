package com.mrgelatine.persona.data

import javax.inject.Inject

class PersonARepository @Inject constructor(
    private val faceDataDAO: FaceDataDAO
){
    suspend fun getAll() = faceDataDAO.getAll()
}
