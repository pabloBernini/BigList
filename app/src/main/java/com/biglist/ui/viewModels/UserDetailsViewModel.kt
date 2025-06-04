package com.biglist.ui.viewModels


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.biglist.util.dataStore
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import android.util.Log

class UserDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context = application.applicationContext

    private val NAME_KEY = stringPreferencesKey("user_name")
    private val SURNAME_KEY = stringPreferencesKey("user_surname")
    private val PHOTO_PATH_KEY = stringPreferencesKey("photo_path")

    private val _name = MutableStateFlow("")
    private val _surname = MutableStateFlow("")
    private val _photoPath = MutableStateFlow<String?>(null)

    val name: StateFlow<String> = _name.asStateFlow()
    val surname: StateFlow<String> = _surname.asStateFlow()
    val photoPath: StateFlow<String?> = _photoPath.asStateFlow()

    init {
        Log.d("UserDetailsViewModel", "ViewModel init block started, collecting from DataStore...")
        viewModelScope.launch {
            context.dataStore.data
                .map { it[NAME_KEY] ?: "" }
                .collect { value ->
                    _name.value = value
                    Log.d("UserDetailsViewModel", "Collected name from DataStore: '$value'")
                }
        }

        viewModelScope.launch {
            context.dataStore.data
                .map { it[SURNAME_KEY] ?: "" }
                .collect { value ->
                    _surname.value = value
                    Log.d("UserDetailsViewModel", "Collected surname from DataStore: '$value'")
                }
        }

        viewModelScope.launch {
            context.dataStore.data
                .map { it[PHOTO_PATH_KEY] }
                .collect { path ->
                    _photoPath.value = path
                    Log.d("UserDetailsViewModel", "Collected photoPath from DataStore: '$path'")
                }
        }
    }

    fun saveUserDetails(name: String, surname: String, photoPath: String?) {
        _name.value = name
        _surname.value = surname
        _photoPath.value = photoPath

        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[NAME_KEY] = name
                prefs[SURNAME_KEY] = surname
                if (photoPath != null) {
                    prefs[PHOTO_PATH_KEY] = photoPath
                } else {
                    prefs.remove(PHOTO_PATH_KEY)
                }
                Log.d(
                    "UserDetailsViewModel",
                    "Saving details to DataStore: Name='$name', Surname='$surname', PhotoPath='$photoPath'"
                )
            }
        }
    }

    fun clearPhoto() {
        _photoPath.value = null
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs.remove(PHOTO_PATH_KEY)
                Log.d("UserDetailsViewModel", "Cleared photo path in DataStore.")
            }
        }
    }
}