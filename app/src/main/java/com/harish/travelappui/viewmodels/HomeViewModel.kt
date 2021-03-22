package com.harish.travelappui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harish.travelappui.models.CitiesResponse
import com.harish.travelappui.repository.CitiesRepository

class HomeViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CitiesRepository(app)
    val events = MutableLiveData<String>()
    val cities = MutableLiveData<CitiesResponse>()


    fun getCities() {
        repository.getCities(onApiCallback = { status, message, response ->
            if (status) {
                cities.postValue(response)

            } else
                events.postValue("$message")
        })
    }


}