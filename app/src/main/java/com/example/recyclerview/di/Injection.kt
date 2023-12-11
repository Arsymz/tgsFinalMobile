package com.example.recyclerview.di

import android.content.Context
import com.example.recyclerview.data.UsersRepository
import com.example.recyclerview.data.prefer.UserPreference
import com.example.recyclerview.data.prefer.dataStore

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val prefer= UserPreference.getInstance(context.dataStore)
        return UsersRepository.getInstance(prefer)
    }
}