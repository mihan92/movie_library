package com.mihan.movie.library.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mihan.movie.library.data.models.VideoHistoryDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoHistoryDao {

    @Query("SELECT * FROM video_history ORDER BY watching_time DESC")
    fun getVideoHistoryList(): Flow<List<VideoHistoryDbModel>>

    @Query("SELECT * FROM video_history WHERE video_id = :videoId")
    fun getVideoHistoryById(videoId: String): Flow<VideoHistoryDbModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateVideoHistory(videoHistoryDbModel: VideoHistoryDbModel)

    @Query("DELETE FROM video_history WHERE video_id = :videoId")
    suspend fun deleteVideoHistoryById(videoId: String)
}