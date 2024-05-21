package com.mrgelatine.persona.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PersonARoomPagingSource(): PagingSource<Int, FaceDataEntity>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FaceDataEntity> {
        TODO("Not yet implemented")
    }

    override fun getRefreshKey(state: PagingState<Int, FaceDataEntity>): Int? {
        TODO("Not yet implemented")
    }
}