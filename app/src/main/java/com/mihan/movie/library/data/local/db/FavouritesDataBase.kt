package com.mihan.movie.library.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mihan.movie.library.data.models.FavouritesDbModel

@Database(entities = [FavouritesDbModel::class], version = 1, exportSchema = false)
abstract class FavouritesDataBase: RoomDatabase() {

    abstract fun favouritesDao(): FavouritesDao
}