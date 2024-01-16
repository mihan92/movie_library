package com.mihan.movie.library.common.receivers

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mihan.movie.library.common.Constants
import com.mihan.movie.library.common.utils.IDownloadManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint(BroadcastReceiver::class)
class DownloadManagerBroadcastReceiver : Hilt_DownloadManagerBroadcastReceiver() {

    @Inject
    internal lateinit var downloadManager: IDownloadManager

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, Constants.DEFAULT_LONG)
        if (intent?.action == Constants.ACTION_DOWNLOAD_COMPLETE && id != null && id != Constants.DEFAULT_LONG) {
            downloadManager.installApk()
        }
    }
}