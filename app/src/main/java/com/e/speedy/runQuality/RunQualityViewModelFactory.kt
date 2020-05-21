package com.e.speedy.runQuality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.e.speedy.database.RunDAO
import java.lang.IllegalArgumentException

class RunQualityViewModelFactory(
    private val runKey: Long,
    private val datasource: RunDAO): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunQualityViewModel::class.java)) {
            return RunQualityViewModel(runKey, datasource) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }
}