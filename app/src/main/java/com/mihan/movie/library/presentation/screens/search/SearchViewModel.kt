package com.mihan.movie.library.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.utils.VoiceRecognizer
import com.mihan.movie.library.domain.usecases.GetVideosByTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val voiceRecognizer: VoiceRecognizer,
    private val getVideosByTitleUseCase: GetVideosByTitleUseCase
) : ViewModel() {
    private val _screenState = MutableStateFlow(SearchScreenState())

    val screenState = _screenState.asStateFlow()
    val voiceState = voiceRecognizer.voiceRecognizerState

    fun startListening() {
        voiceRecognizer.startListening("ru")
    }

    fun stopListening() {
        voiceRecognizer.stopListening()
    }

    fun buttonSearchPressed(searchngText: String) {
        if (searchngText.isNotEmpty()) {
            viewModelScope.launch {
                getVideosByTitleUseCase(searchngText)
                    .onEach { state ->
                        when (state) {
                            is DtoState.Error -> _screenState.update { it.copy(error = state.errorMessage) }
                            is DtoState.Loading -> _screenState.update { it.copy(isLoading = true) }
                            is DtoState.Success ->
                                _screenState.update { it.copy(isLoading = false, listOfVideo = state.data) }
                        }
                    }.last()
                voiceRecognizer.resetState()
            }
        }
    }
}