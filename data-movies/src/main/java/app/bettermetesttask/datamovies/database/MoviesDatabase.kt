package app.bettermetesttask.datamovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.bettermetesttask.datamovies.database.dao.MoviesDao
import app.bettermetesttask.datamovies.database.entities.LikedMovieEntity
import app.bettermetesttask.datamovies.database.entities.MovieEntity

const val DB_NAME = "movies_database.db"

@Database(version = 1, entities = [MovieEntity::class, LikedMovieEntity::class])
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao

    companion object {
        @Volatile
        private var instance: MoviesDatabase? = null

        fun initDBInstance(context: Context) {
            instance ?: synchronized(this) {
                buildDatabase(context).also { instance = it }
            }
        }

        fun getDBInstance(context: Context): MoviesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(context.applicationContext, MoviesDatabase::class.java, "smartScale_db")
//                .addMigrations()
                .build()
        }
    }
}