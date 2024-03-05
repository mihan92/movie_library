package com.mihan.movie.library.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mihan.movie.library.data.models.FavouritesDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM video_favourites")
    fun getFavourites(): Flow<List<FavouritesDbModel>>

    @Query("SELECT * FROM video_favourites WHERE video_id = :videoId")
    fun getFavouriteVideoById(videoId: String): Flow<FavouritesDbModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(favouritesDbModel: FavouritesDbModel)

    @Query("DELETE FROM video_favourites WHERE video_id = :videoId")
    suspend fun deleteFromFavourites(videoId: String)
}