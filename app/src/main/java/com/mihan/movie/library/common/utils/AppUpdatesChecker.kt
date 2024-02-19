package com.mihan.movie.library.common.utils

import com.mihan.movie.library.BuildConfig
import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.common.DataStorePrefs
import com.mihan.movie.library.common.DtoState
import com.mihan.movie.library.common.extentions.logger
import com.mihan.movie.library.domain.models.ChangelogModel
import com.mihan.movie.library.domain.usecases.CheckUpdatesUseCase
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ActivityRetainedScoped
class AppUpdatesChecker @Inject constructor(
    private val checkUpdatesUseCase: CheckUpdatesUseCase,
    private val dataStorePrefs: DataStorePrefs
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    var changelog: ChangelogModel? = null
        private set

    fun checkUpdates() {
        launch {
            checkUpdatesUseCase()
                .onEach { state ->
                    when (state) {
                        is DtoState.Error -> logger(state.errorMessage)
                        is DtoState.Loading -> Unit
                        is DtoState.Success -> {
                            val latestVersionCode = state.data?.latestVersionCode ?: Constants.DEFAULT_INT
                            dataStorePrefs.setAppUpdates(latestVersionCode > BuildConfig.VERSION_CODE)
                            changelog = state.data
                        }
                    }
                }.last()
        }
    }
}