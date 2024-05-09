package com.mrgelatine.persona.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FaceDataEntity::class], version = 1)
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