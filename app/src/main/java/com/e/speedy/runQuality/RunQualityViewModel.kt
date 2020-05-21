package com.e.speedy.runQuality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.speedy.database.RunDAO
import kotlinx.coroutines.*

class RunQualityViewModel(private val runKey: Long, val databaseDAO: RunDAO): ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToRunTracker = MutableLiveData<Boolean?>()
    val navigateToRunTracker: LiveData<Boolean?>
        get() = _navigateToRunTracker

    fun doneNavigating() {
        _navigateToRunTracker.value = null
    }

    fun onSetRunQuality(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val currentRun = databaseDAO.get(runKey) ?: return@withContext
                currentRun.runQuality = quality
                databaseDAO.update(currentRun)
            }
            _navigateToRunTracker.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}