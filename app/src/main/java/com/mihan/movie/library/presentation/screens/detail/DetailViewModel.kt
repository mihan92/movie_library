package com.mihan.movie.library.presentation.screens.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.domain.models.StreamModel
import com.mihan.movie.library.domain.models.VideoModel
import com.mihan.movie.library.domain.usecases.GetDetailVideoByUrlUseCase
import com.mihan.movie.library.domain.usecases.GetStreamsBySeasonIdUseCase
import com.mihan.movie.library.domain.usecases.GetTranslationsByUrlUseCase
import com.mihan.movie.library.presentation.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailVideoByUrlUseCase: GetDetailVideoByUrlUseCase,
    private val getTranslationsByUrlUseCase: GetTranslationsByUrlUseCase,
    private val getStreamsBySeasonIdUseCase: GetStreamsBySeasonIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val navArgs: DetailScreenNavArgs = savedStateHandle.navArgs()
    private val _screenState = MutableStateFlow(DetailScreenState())
    private val _showFilmDialog = MutableStateFlow(false)
    private val _showSerialDialog = MutableStateFlow(false)
    private val _listOfStreams = MutableStateFlow<List<StreamModel>?>(null)
    private val _videoData = MutableStateFlow(VideoModel())
    private var _translatorId = _videoData.value.translations.values.firstOrNull()

    val videoData = _videoData.asStateFlow()
    val showFilmDialog = _showFilmDialog.asStateFlow()
    val showSerialDialog = _showSerialDialog.asStateFlow()
    val screenState = _screenState.asStateFlow()
    val listofStreams = _listOfStreams.asStateFlow()

    init {
        viewModelScope.launch {
            getDetailVideoByUrlUseCase(navArgs.movieUrl)
                .onEach { state ->
                    when (state) {
                        is DtoState.Error -> _screenState.value = DetailScreenState(errorMessage = state.errorMessage)
                        is DtoState.Loading -> _screenState.value = DetailScreenState(isLoading = true)
                        is DtoState.Success -> _screenState.value = DetailScreenState(detailInfo = state.data)
                    }
                }.last()
        }
    }

    private fun updateData() {
        _videoData.value.let { videoModel ->
            if (videoModel.seasonList.isEmpty() && !videoModel.isVideoHasTranslations) {
                //Фильм без переводов
                val defaultTranslate = videoModel.videoStreamsWithTranslatorName.values.first()
                _listOfStreams.value = defaultTranslate
            } else if (videoModel.seasonList.isEmpty()) {
                //Фильм с переводами
                showFilmDialog()
            } else if (videoModel.isVideoHasSeries && !videoModel.isVideoHasTranslations) {
                //Сериал без переводов
                showSerialDialog()
            } else if (videoModel.isVideoHasSeries) {
                //Сериал с переводами
                showSerialDialog()
            }
        }
    }

    fun getTranslations() {
        viewModelScope.launch {
            getTranslationsByUrlUseCase(navArgs.movieUrl)
                .onEach { state ->
                    when (state) {
                        is DtoState.Error -> _screenState.value = DetailScreenState(
                            detailInfo = _screenState.value.detailInfo,
                            errorMessage = state.errorMessage
                        )

                        is DtoState.Loading -> _screenState.value = DetailScreenState(
                            detailInfo = _screenState.value.detailInfo,
                            isLoading = true
                        )

                        is DtoState.Success -> {
                            _screenState.value = DetailScreenState(detailInfo = _screenState.value.detailInfo)
                            _videoData.value = state.data!!
                            _translatorId = state.data.translations.values.first()
                            updateData()
                        }
                    }
                }.last()
        }
    }

    private fun getStreamsBySeasonId(translationId: String, videoId: String, season: String, episode: String) {
        viewModelScope.launch {
            getStreamsBySeasonIdUseCase(translationId, videoId, season, episode)
                .onEach { state ->
                    when (state) {
                        is DtoState.Error -> _screenState.value = DetailScreenState(
                            detailInfo = _screenState.value.detailInfo,
                            errorMessage = state.errorMessage
                        )

                        is DtoState.Loading -> _screenState.value = DetailScreenState(
                            detailInfo = _screenState.value.detailInfo,
                            isLoading = true
                        )

                        is DtoState.Success -> {
                            _screenState.value = DetailScreenState(detailInfo = _screenState.value.detailInfo)
                            _listOfStreams.value = state.data!!
                        }
                    }
                }.last()
        }
    }

    fun selectedTranslate(translate: List<StreamModel>) {
        _listOfStreams.value = translate
    }

    fun selectedTranslatorId(translationId: String) {
        _translatorId = translationId
    }

    fun onEpisodeClicked(season: String, episode: String) {
        _translatorId?.let { id ->
            getStreamsBySeasonId(id, _videoData.value.videoId, season, episode)
        }
    }

    private fun showSerialDialog() {
        _showSerialDialog.value = true
    }

    private fun showFilmDialog() {
        _showFilmDialog.value = true
    }

    fun onDialogDismiss() {
        _showFilmDialog.value = false
        _showSerialDialog.value = false
    }

    fun sendIntent(selectedStream: List<StreamModel>, context: Context) {
        try {
            val maxQuality = selectedStream.last()
            val videoPath = maxQuality.url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoPath))
            intent.setDataAndType(Uri.parse(videoPath), "video/*")
            intent.putExtra("title", screenState.value.detailInfo?.title)
            context.startActivity(intent)
        } catch (e: Exception) {
            _screenState.value = DetailScreenState(
                detailInfo = _screenState.value.detailInfo,
                errorMessage = e.message.toString()
            )
        }
    }
}