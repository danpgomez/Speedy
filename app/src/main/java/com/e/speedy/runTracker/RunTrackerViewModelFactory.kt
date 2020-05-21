package com.e.speedy.runTracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.e.speedy.database.RunDAO
import java.lang.IllegalArgumentException

class RunTrackerViewModelFactory(
    private val dataSource: RunDAO,
    private val application: Application): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunTrackerViewModel::class.java)) {
            return RunTrackerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}