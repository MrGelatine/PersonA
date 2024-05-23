package com.mrgelatine.persona.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FaceDataDAO{
    @Query("SELECT * FROM FACE_DATA")
    fun getAll(): Flow<List<FaceDataEntity>>

    @Query("SELECT * FROM FACE_DATA")
    fun getAllPaging(): PagingSource<Int, FaceDataEntity>

    @Insert
    fun insert(face: FaceDataEntity)

    @Delete
    fun delete(face: FaceDataEntity)
}