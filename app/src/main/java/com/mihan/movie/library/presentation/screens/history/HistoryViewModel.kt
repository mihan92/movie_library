package com.mihan.movie.library.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.common.DataStorePrefs
import com.mihan.movie.library.common.utils.whileUiSubscribed
import com.mihan.movie.library.domain.models.VideoHistoryModel
import com.mihan.movie.library.domain.usecases.DeleteVideoHistoryByIdUseCase
import com.mihan.movie.library.domain.usecases.GetVideoHistoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getVideoHistoryListUseCase: GetVideoHistoryListUseCase,
    private val deleteVideoHistoryByIdUseCase: DeleteVideoHistoryByIdUseCase,
    private val dataStorePrefs: DataStorePrefs
) : ViewModel() {
    private val _historyList = MutableStateFlow<List<VideoHistoryModel>>(emptyList())
    val historyList = _historyList.asStateFlow()
    val baseUrl = dataStorePrefs.getBaseUrl().stateIn(viewModelScope, whileUiSubscribed, Constants.EMPTY_STRING)

    init {
        getHistoryList()
    }

    private fun getHistoryList() {
        viewModelScope.launch {
            getVideoHistoryListUseCase().onEach { list ->
                _historyList.update { list }
            }.launchIn(this)
        }
    }

    fun onButtonDeleteClicked(videoId: String) {
        viewModelScope.launch {
            deleteVideoHistoryByIdUseCase(videoId)
        }
    }
}