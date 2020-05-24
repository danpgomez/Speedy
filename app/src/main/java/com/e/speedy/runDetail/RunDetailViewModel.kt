package com.e.speedy.runDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.speedy.database.Run
import com.e.speedy.database.RunDAO
import kotlinx.coroutines.Job


class RunDetailViewModel(
    private val runKey: Long = 0L,
    dataSource: RunDAO
) : ViewModel() {

    val database = dataSource
    private val viewModelJob = Job()
    private val run = MediatorLiveData<Run>()
    fun getRun() = run

    init {
        run.addSource(database.getRunWithId(runKey), run::setValue)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Code for navigating to runTracker fragment
    private val _navigateToRunTracker = MutableLiveData<Boolean?>()
    val navigateToRunTracker: LiveData<Boolean?>
        get() = _navigateToRunTracker

    fun doneNavigating() {
        _navigateToRunTracker.value = false
    }

    fun onClose() {
        _navigateToRunTracker.value = true
    }


}
