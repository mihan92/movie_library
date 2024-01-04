package com.mihan.movie.library.presentation.screens.app_updates

import androidx.lifecycle.ViewModel
import com.mihan.movie.library.common.utils.AppUpdatesChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppUpdatesViewModel @Inject constructor(
    private val appUpdatesChecker: AppUpdatesChecker
): ViewModel() {

    val changelogModel = appUpdatesChecker.changelog

    fun buttonUpdatePressed() {

    }
}