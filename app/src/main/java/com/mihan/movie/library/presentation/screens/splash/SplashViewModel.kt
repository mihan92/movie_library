package com.mihan.movie.library.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.DataStorePrefs
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.domain.usecases.GetListVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getListVideoUseCase: GetListVideoUseCase,
    private val dataStorePrefs: DataStorePrefs
) : ViewModel() {

    private val _screenState = MutableStateFlow(SplashScreenState())
    val splashScreenState = _screenState.asStateFlow()

    init {
        getListVideo()
    }

    private fun getListVideo() {
        viewModelScope.launch {
            val videoCategory = dataStorePrefs.getVideoCategory().first()
            val baseUrl = dataStorePrefs.getBaseUrl().first()
            getListVideoUseCase(Filter.Watching, videoCategory, FIRST_PAGE, baseUrl).onEach { dtoState ->
                when (dtoState) {
                    is DtoState.Error -> _screenState.value = SplashScreenState(errorMessage = dtoState.errorMessage)
                    is DtoState.Loading -> Unit
                    is DtoState.Success -> _screenState.value = SplashScreenState(success = true)
                }
            }.last()
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}