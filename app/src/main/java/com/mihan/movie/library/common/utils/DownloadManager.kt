package com.mihan.movie.library.common.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.mihan.movie.library.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface IDownloadManager {
    fun downloadApk(url: String): Long

    fun installApk()
}

@Singleton
class DownloadManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloadManager: DownloadManager
) : IDownloadManager {

    private var downloadedFilePath: File? = null

    /**
     * Перед загрузкой новой apk старые удаляются из кэша, если таковые имеются
     */
    private fun deleteOldApk() {
        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path?.let { path ->
            val downloadsDirectory = File(path)
            if (downloadsDirectory.exists()) {
                downloadsDirectory.listFiles()?.forEach { file ->
                    file.delete()
                }
            }
        }
    }

    override fun downloadApk(url: String): Long {
        val filename = url.substringAfterLast("/")
        downloadedFilePath = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename)
        deleteOldApk()
        val request = DownloadManager.Request(url.toUri())
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setMimeType(Constants.MIME_TYPE)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename)
        return downloadManager.enqueue(request)
    }

    override fun installApk() {
        downloadedFilePath?.let { filePath ->
            val fileUri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", filePath)
            val intent = Intent(Intent.ACTION_VIEW, fileUri).apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                setDataAndType(fileUri, Constants.MIME_TYPE)
            }
            context.startActivity(intent)
        }
    }
}