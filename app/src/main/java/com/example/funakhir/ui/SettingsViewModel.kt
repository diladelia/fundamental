package com.example.funakhir.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.funakhir.data.SettingsPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val settingsPreferences: SettingsPreferences

    init {
        settingsPreferences = SettingsPreferences.getInstance(application)
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        settingsPreferences.saveThemeSetting(isDarkModeActive)
    }

    fun getThemeSetting() = settingsPreferences.getThemeSetting().asLiveData(Dispatchers.IO)
}