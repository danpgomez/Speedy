package com.e.speedy.runTracker

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.e.speedy.database.Run
import com.e.speedy.database.RunDAO
import com.e.speedy.formatRuns
import kotlinx.coroutines.*

class RunTrackerViewModel(
    val databaseDAO: RunDAO,
    application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var currentRun = MutableLiveData<Run?>()
    private var allRuns = databaseDAO.getAllRuns()
    val allRunsString = Transformations.map(allRuns) { runList ->
        formatRuns(runList, application.resources)
    }

    private val _navigateToRunQuality = MutableLiveData<Run>()
    val navigateToRunQuality: LiveData<Run>
        get() = _navigateToRunQuality

    fun doneNavigating() {
        _navigateToRunQuality.value = null
    }

    init {
        initializeCurrentRun()
    }

    val startButtonVisible: LiveData<Boolean> = Transformations.map(currentRun) {
        null == it
    }

    val stopButtonVisible: LiveData<Boolean> = Transformations.map(currentRun) {
        null != it
    }

    val clearButtonVisible: LiveData<Boolean?> = Transformations.map(allRuns) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    private fun initializeCurrentRun() {
        uiScope.launch {
            currentRun.value = getCurrentRunFromDatabase()
        }
    }

    private suspend fun getCurrentRunFromDatabase(): Run? {
        return withContext(Dispatchers.IO) {
            var run = databaseDAO.getCurrentRun()
            if (run?.endTimeMillis != run?.startTimeMillis) {
                run = null
            }
            run
        }
    }

    fun onStarTracking() {
        uiScope.launch {
            val newRun = Run()
            insert(newRun)
            currentRun.value = getCurrentRunFromDatabase()
        }
    }

    private suspend fun insert(run: Run) {
        withContext(Dispatchers.IO) {
            databaseDAO.insert(run)
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldRun = currentRun.value ?: return@launch
            oldRun.endTimeMillis = System.currentTimeMillis()
            update(oldRun)
            _navigateToRunQuality.value = oldRun
        }
    }

    private suspend fun update(run: Run) {
        withContext(Dispatchers.IO) {
            databaseDAO.update(run)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            currentRun.value = null
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            databaseDAO.clear()
        }
    }
}