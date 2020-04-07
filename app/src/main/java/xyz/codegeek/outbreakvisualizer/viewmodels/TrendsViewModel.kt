package xyz.codegeek.outbreakvisualizer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import xyz.codegeek.outbreakvisualizer.data.database.RegionalData
import xyz.codegeek.outbreakvisualizer.data.repository.Repository
import xyz.codegeek.outbreakvisualizer.helpers.Result
import java.io.IOException

class TrendsViewModel(private val repo: Repository) : ViewModel(){
    private val _regionalData = MutableLiveData<List<RegionalData>>()
    val regionalData: LiveData<List<RegionalData>> get() = _regionalData

    private var initJob: Job? = null

    private val _snackbar = MutableLiveData<String?>()
    val snackbar: LiveData<String?> get() = _snackbar
    private val _spinner = MutableLiveData<Boolean>(true)
    val spinner: LiveData<Boolean> get() = _spinner

    init {
        initializeData()
    }

    private fun initializeData() {
        initJob?.cancel()
        initJob = launchDataLoad { fetchandUpdateData() }
    }

    private suspend fun fetchandUpdateData() {
        _regionalData.value = withContext(Dispatchers.IO){
            if(repo.shouldUpdateCache()){
                val result = async {  repo.fetchremoteData()}.await()
                if (result is Result.Success){
                    repo.saveRemoteDataSource(result)
                }else if(result is Result.Error){
                    throw result.exception
                }
            }
            getRegionalDataFromDatabase()
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job{
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: IOException){
                _snackbar.value = error.message
            }finally {
                _spinner.value = false
            }
        }
    }

    private suspend fun getRegionalDataFromDatabase(): List<RegionalData> {
        return repo.getData()
    }

    fun onSnackbarShown(){
        _snackbar.value = null
    }


}