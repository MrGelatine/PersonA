package com.mrgelatine.persona.data

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FACE_DATA")
data class FaceDataEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name="features") var featureList: Map<String, Float>,
    @ColumnInfo(name="embedding") var rawEmbedding: List<Float>,
    @ColumnInfo(name="image") var image: String
)