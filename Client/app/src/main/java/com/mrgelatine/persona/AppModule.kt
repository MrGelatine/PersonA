package com.mrgelatine.persona

import android.content.Context
import androidx.room.Room
import com.mrgelatine.persona.data.PersonADatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        PersonADatabase::class.java,
        "your_db_name"
    ).build()

    @Singleton
    @Provides
    fun provideYourDao(db: PersonADatabase) = db.faceDataDAO()
}