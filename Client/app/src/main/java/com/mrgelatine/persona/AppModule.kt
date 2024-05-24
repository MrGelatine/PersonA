package com.mrgelatine.persona

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mrgelatine.persona.data.PersonADatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideYourContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun providePersonADatabase(
        @ApplicationContext appContext: Context
    ): PersonADatabase {
        return Room.databaseBuilder(
            appContext,
            PersonADatabase::class.java,
            "face_database"
        ).build()
    }
    @Singleton
    @Provides
    fun provideFaceDataDAO(db: PersonADatabase) = db.faceDataDAO()


}