package com.mrgelatine.persona.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

@Database(entities = [FaceDataEntity::class], version = 1)
@TypeConverters(MapConverter::class)
abstract class PersonADatabase: RoomDatabase(){
    abstract fun faceDataDAO(): FaceDataDAO

    companion object {
        @Volatile
        private var Instance: PersonADatabase? = null

        fun getDatabase(context: Context): PersonADatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PersonADatabase::class.java, "face_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

class MapConverter{
    val converter = Gson()
    @TypeConverter
    fun mapToString(map: Map<String,Float>): String?{
        return converter.toJson(map)
    }
    @TypeConverter
    fun stringToMap(str: String?): Map<String,Float>{
        val mapType = object : TypeToken<Map<String,Float>>() {}.type
        return converter.fromJson(str, mapType)
    }

    @TypeConverter
    fun stringToList(str:String?): List<Float>{
        val mapType = object : TypeToken<List<Float>>() {}.type
        return converter.fromJson(str, mapType)
    }

    @TypeConverter
    fun listToString(lst: List<Float>): String?{
        return converter.toJson(lst)
    }
}