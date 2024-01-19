package com.mihan.movie.library.presentation.screens.detail

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mihan.movie.library.common.Constants.EMPTY_STRING
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.domain.models.SeasonModel
import com.mihan.movie.library.domain.models.StreamModel
import com.mihan.movie.library.domain.models.VideoModel
import com.mihan.movie.library.domain.usecases.GetDetailVideoByUrlUseCase
import com.mihan.movie.library.domain.usecases.GetSeasonsByTranslatorIdUseCase
import com.mihan.movie.library.domain.usecases.GetStreamsBySeasonIdUseCase
import com.mihan.movie.library.domain.usecases.GetTranslationsByUrlUseCase
import com.mihan.movie.library.presentation.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailVideoByUrlUseCase: GetDetailVideoByUrlUseCase,
    private val getTranslationsByUrlUseCase: GetTranslationsByUrlUseCase,
    private val getStreamsBySeasonIdUseCase: GetStreamsBySeasonIdUseCase,
    private val getSeasonsByTranslatorIdUseCase: GetSeasonsByTranslatorIdUseCase,
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val navArgs: DetailScreenNavArgs = savedStateHandle.navArgs()
    private val _screenState = MutableStateFlow(DetailScreenState())
    private val _showFilmDialog = MutableStateFlow(false)
    private val _showSerialDialog = MutableStateFlow(false)
    private val _listOfStreams = MutableStateFlow<List<StreamModel>>(emptyList())
    private val _videoData = MutableStateFlow(VideoModel())
    private var _translatorId = _videoData.value.translations.values.firstOrNull()
    private val _listOfSeasons = MutableStateFlow<List<SeasonModel>>(emptyList())
    private var _seasonAndEpisodeTitle = Pair(EMPTY_STRING, EMPTY_STRING)

    val videoData = _videoData.asStateFlow()
    val showFilmDialog = _showFilmDialog.asStateFlow()
    val showSerialDialog = _showSerialDialog.asStateFlow()
    val screenState = _screenState.asStateFlow()
    val listOfSeasons = _listOfSeasons.asStateFlow()

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
        updateListOfStreams()
    }

    private fun updateData() {
        if (_listOfSeasons.value.isEmpty() && !_videoData.value.isVideoHasTranslations) {
            //Фильм без переводов
            val defaultTranslate = _videoData.value.videoStreamsWithTranslatorName.values.first()
            _listOfStreams.update { defaultTranslate }
        } else if (_listOfSeasons.value.isEmpty()) {
            //Фильм с переводами
            showFilmDialog()
        } else if (_videoData.value.isVideoHasSeries && !_videoData.value.isVideoHasTranslations) {
            //Сериал без переводов
            showSerialDialog()
        } else if (_videoData.value.isVideoHasSeries) {
            //Сериал с переводами
            showSerialDialog()
        }
    }

    private fun getSeasonsByTranslatorId(translatorId: String) {
        viewModelScope.launch {
            getSeasonsByTranslatorIdUseCase(translatorId)
                .onEach { state ->
                    when (state) {
                        is DtoState.Error -> _screenState.value = DetailScreenState(
                            detailInfo = _screenState.value.detailInfo,
                            errorMessage = state.errorMessage
                        )

                        is DtoState.Loading -> Unit
                        is DtoState.Success -> {
                            _listOfSeasons.update { state.data ?: emptyList() }
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
                            _listOfStreams.value = state.data ?: emptyList()
                        }
                    }
                }.last()
        }
    }

    private fun updateListOfStreams() {
        _listOfStreams.onEach { list ->
            if (list.isNotEmpty())
                sendIntent(list)
        }.launchIn(viewModelScope)
    }

    private fun sendIntent(selectedStream: List<StreamModel>) {
        try {
            val context = getApplication<Application>().applicationContext
            val maxQuality = selectedStream.last()
            val videoPath = maxQuality.url
            val title =
                if (!_videoData.value.isVideoHasSeries) screenState.value.detailInfo?.title
                else
                    "${screenState.value.detailInfo?.title}" +
                            "  /  ${_seasonAndEpisodeTitle.first} сезон" +
                            "  ${_seasonAndEpisodeTitle.second} серия"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoPath)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setDataAndType(Uri.parse(videoPath), "video/*")
                putExtra("title", title)
            }
            context.startActivity(intent)
            _listOfStreams.update { emptyList() }
        } catch (e: Exception) {
            _screenState.value = DetailScreenState(
                detailInfo = _screenState.value.detailInfo,
                errorMessage = e.message.toString()
            )
        }
    }

    private fun showSerialDialog() {
        _showSerialDialog.value = true
    }

    private fun showFilmDialog() {
        _showFilmDialog.value = true
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
                            if (state.data.isVideoHasSeries)
                                getSeasonsByTranslatorId(state.data.translations.values.first())
                            else
                                updateData()
                        }
                    }
                }.last()
        }
    }

    fun selectedTranslate(translate: List<StreamModel>) {
        _listOfStreams.update { translate }
    }

    fun selectedTranslatorId(translationId: String) {
        _translatorId = translationId
        getSeasonsByTranslatorId(translationId)
    }

    fun onEpisodeClicked(season: String, episode: String) {
        _seasonAndEpisodeTitle = season to episode
        _translatorId?.let { id ->
            getStreamsBySeasonId(id, _videoData.value.videoId, season, episode)
        }
    }

    fun onDialogDismiss() {
        _showFilmDialog.value = false
        _showSerialDialog.value = false
    }
}