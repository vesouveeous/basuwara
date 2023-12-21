package com.dicoding.basuwara.di

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.dicoding.basuwara.data.preferences.UserPreference
import com.dicoding.basuwara.data.preferences.dataStore
import com.dicoding.basuwara.data.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase,
        scope: CoroutineScope,
        pref: UserPreference
    ) = AuthRepository(firebaseAuth, firebaseDatabase, pref)

    @Provides
    @Singleton
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun providePreference(@ApplicationContext context: Context) = UserPreference.getInstance(context.dataStore)
}