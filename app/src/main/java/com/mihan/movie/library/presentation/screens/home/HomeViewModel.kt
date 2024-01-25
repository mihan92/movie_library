package com.mihan.movie.library.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.DataStorePrefs
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.common.utils.whileUiSubscribed
import com.mihan.movie.library.domain.usecases.GetListVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListVideoUseCase: GetListVideoUseCase,
    dataStorePrefs: DataStorePrefs
) : ViewModel() {
    private val _screenState = MutableStateFlow(HomeScreenState())
    private val _filterState = MutableStateFlow(Filter.Watching)
    private val _pageState = MutableStateFlow(FIRST_PAGE)
    private val _videoCategoryState = dataStorePrefs.getVideoCategory().shareIn(
        viewModelScope,
        whileUiSubscribed,
        replay = 1
    ).distinctUntilChanged()
    private val _baseUrlState = dataStorePrefs.getBaseUrl().shareIn(
        viewModelScope,
        whileUiSubscribed,
        replay = 1
    ).distinctUntilChanged()
    val screenState = _screenState.asStateFlow()
    val filterState = _filterState.asStateFlow()
    val pageState = _pageState.asStateFlow()

    init {
        getListVideo()
    }

    private fun getListVideo() {
        combine(_filterState, _pageState, _videoCategoryState, _baseUrlState) { filter, page, videoCategory, url ->
            getListVideoUseCase.invoke(filter, videoCategory, page, url).onEach { dtoState ->
                when (dtoState) {
                    is DtoState.Error -> _screenState.value = HomeScreenState(errorMessage = dtoState.errorMessage)
                    is DtoState.Loading -> _screenState.value = HomeScreenState(isLoading = true)
                    is DtoState.Success -> _screenState.value = HomeScreenState(data = dtoState.data ?: emptyList())
                }
            }.last()
        }.launchIn(viewModelScope)
    }

    fun onTopBarItemClicked(filter: Filter) {
        _pageState.value = FIRST_PAGE
        _filterState.value = filter
    }

    fun onPageChanged(page: Int) {
        _pageState.value = page
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}