package com.mrgelatine.persona.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

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
    fun stringToFloatList(str:String?): List<Float>{
        val mapType = object : TypeToken<List<Float>>() {}.type
        return converter.fromJson(str, mapType)
    }

    @TypeConverter
    fun stringToStringList(str:String?): List<String>{
        val mapType = object : TypeToken<List<String>>() {}.type
        return converter.fromJson(str, mapType)
    }

    @TypeConverter
    fun floatListToString(lst: List<Float>): String{
        return converter.toJson(lst)
    }

    @TypeConverter
    fun stringListToString(lst: List<String>): String{
        return converter.toJson(lst)
    }

    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun longToDate(long: Long): Date {
        return Date(long)
    }
}