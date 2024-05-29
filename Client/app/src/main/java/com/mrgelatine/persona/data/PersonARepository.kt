package com.mrgelatine.persona.data

import android.content.Context
import android.graphics.Bitmap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import java.io.File
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class PersonARepository @Inject constructor(
    private val faceDataDAO: FaceDataDAO,
    private val context: Context
){
    suspend fun getAll() = faceDataDAO.getAll()
    suspend fun insert(face: FaceDataEntity) = faceDataDAO.insert(face)
    fun saveImageToStorage(bitmap: Bitmap): String{
        val file = File(context.filesDir, UUID.randomUUID().toString()+ ".jpg")
        val savePath: String = file.path
        val outStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        return savePath
    }
    suspend fun insert(featureList: Map<String, Float>, rawEmbedding: List<Float>, image: String, tags: List<String>){
        val face: FaceDataEntity = FaceDataEntity(featureList= featureList, rawEmbedding= rawEmbedding, image= image, tags= tags, added = Date(System.currentTimeMillis()), modified = Date(System.currentTimeMillis()))
        this.insert(face)
    }

    fun observePagingSource(): Flow<PagingData<FaceDataEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20),
            pagingSourceFactory = { faceDataDAO.getAllPaging() }
        ).flow
    }

}
