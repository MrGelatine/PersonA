package com.mrgelatine.persona.data

import android.graphics.Bitmap
import android.media.FaceDetector.Face
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.Dictionary
import kotlin.time.Duration.Companion.seconds

@Entity(tableName = "FACE_DATA")
data class FaceDataEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name="features") var featureList: Map<String, Float>,
    @ColumnInfo(name="embedding") var rawEmbedding: List<Float>,
    @ColumnInfo(name="image") var image: String,
    @ColumnInfo(name="tags") var tags: List<String>,
    @ColumnInfo(name="date_added") var added: Date,
    @ColumnInfo(name="date_modified") var modified: Date,
){
    companion object{
        val SECONDS_DIV = 1000
        val MINUTES_DIV = SECONDS_DIV * 60
        val HOURS_DIV = MINUTES_DIV * 60
        val DAYS_DIV = HOURS_DIV * 24
        val MONTHS_DIV = DAYS_DIV * 30
        val YEARS_DIV = MONTHS_DIV * 365
        fun duration(from: Date, to: Date): Map<String, Int>{
            val timePassed = to.time - from.time
            val years = (timePassed / YEARS_DIV).toInt()
            val months = (timePassed / MONTHS_DIV).toInt()
            val days = (timePassed / DAYS_DIV).toInt()
            val hours = (timePassed / HOURS_DIV).toInt()
            val minutes = (timePassed / MINUTES_DIV).toInt()
            val seconds = (timePassed / SECONDS_DIV).toInt()
            return mapOf(
                Pair("years",years),
                Pair("months",months),
                Pair("days",days),
                Pair("hours",hours),
                Pair("minutes",minutes),
                Pair("seconds",seconds)
            )
        }
    }
}
