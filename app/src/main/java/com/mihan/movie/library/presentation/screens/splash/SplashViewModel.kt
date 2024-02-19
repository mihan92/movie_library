package com.mihan.movie.library.presentation.screens.splash

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.DataStorePrefs
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.entites.Filter
import com.mihan.movie.library.common.utils.IDownloadManager
import com.mihan.movie.library.domain.usecases.GetBaseUrlUseCase
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
    private val dataStorePrefs: DataStorePrefs,
    private val getBaseUrlUseCase: GetBaseUrlUseCase,
    downloadManager: IDownloadManager,
    application: Application
) : AndroidViewModel(application) {

    private val _screenState = MutableStateFlow(SplashScreenState())
    val splashScreenState = _screenState.asStateFlow()

    init {
        getListVideo()
        downloadManager.deleteOldApk()
    }

    private fun getListVideo() {
        viewModelScope.launch {
            //Включить при появлении нормального удаленного сервера парсинга
//            val baseUrl = getBaseUrlUseCase().last().data?.baseUrl
//            if (!dataStorePrefs.getRemoteParsing().first()
//                && !baseUrl.isNullOrEmpty()
//                && baseUrl != dataStorePrefs.getBaseUrl().first()
//            ) {
//                dataStorePrefs.setBaseUrl(baseUrl)
//                val context = getApplication<Application>().applicationContext
//                Toast.makeText(context, "Ссылка на сайт обновлена $baseUrl", Toast.LENGTH_LONG).show()
//            }
            val videoCategory = dataStorePrefs.getVideoCategory().first()
            getListVideoUseCase(Filter.Watching, videoCategory, FIRST_PAGE).onEach { dtoState ->
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