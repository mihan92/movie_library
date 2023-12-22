package com.mihan.movie.library.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.common.entites.VideoCategory
import com.mihan.movie.library.domain.usecases.GetListVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListVideoUseCase: GetListVideoUseCase
): ViewModel() {
    private val _screenState = MutableStateFlow(HomeScreenState())
    private val _filterState = MutableStateFlow(Filter.Watching)
    private val _pageState = MutableStateFlow(FIRST_PAGE)
    val screenState = _screenState.asStateFlow()
    val filterState = _filterState.asStateFlow()
    val pageState = _pageState.asStateFlow()
    init {
        getData()
    }
    private fun getData() {
        viewModelScope.launch {
            _filterState.combine(_pageState) { filter, page ->
                getListVideoUseCase(filter, VideoCategory.All, page).onEach { dtoState ->
                    when(dtoState){
                        is DtoState.Error -> _screenState.value = HomeScreenState(errorMessage = dtoState.errorMessage)
                        is DtoState.Loading -> _screenState.value = HomeScreenState(isLoading = true)
                        is DtoState.Success -> _screenState.value = HomeScreenState(data = dtoState.data ?: emptyList())
                    }
                }.last()
            }.stateIn(viewModelScope)
        }
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