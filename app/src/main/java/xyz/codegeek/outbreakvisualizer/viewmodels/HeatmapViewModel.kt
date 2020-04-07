package xyz.codegeek.outbreakvisualizer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Feature
import kotlinx.coroutines.*
import xyz.codegeek.outbreakvisualizer.data.repository.Repository

class HeatmapViewModel(private val repo: Repository): ViewModel() {

    private val _regionalFeatures = MutableLiveData<List<Feature>>()
    val regionalFeatures: LiveData<List<Feature>> get() = _regionalFeatures

    private var initJob: Job? = null

    private val _snackbar = MutableLiveData<String?>()
    private val _spinner = MutableLiveData<Boolean>(false)

    init {
        initializeData()
    }

    private fun initializeData() {
        initJob?.cancel()
        initJob = launchDataLoad { fetchandUpdateData() }
    }

    private suspend fun fetchandUpdateData() {
        _regionalFeatures.value = withContext(Dispatchers.IO){
            /**
            if(repo.shouldUpdateCache()){
                val result = repo.fetchremoteData()
                if (result is Result.Success){

                    repo.saveRemoteDataSource(result)
                }else{
                    throw IOException("Unable to fetch data from the internet")
                }
            }
            **/
            getRegionalFeaturesFromDatabase()
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job{
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: Throwable){
                _snackbar.value = error.message
            }finally {
                _spinner.value = false
            }
        }
    }

    private suspend fun getRegionalFeaturesFromDatabase(): List<Feature> {
        return repo.getFeatures()
    }

}