package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val localStore: MoviesLocalStore,
    private val mapper: MoviesMapper
) : MoviesRepository {

    private val restStore = MoviesRestStore()

    override suspend fun getMovies(): Result<List<Movie>> {
        return Result.of {
            Timber.i("loadMovies: ${localStore.getMovies()}")
            localStore.getMovies().map { item ->
                item.let {
                    Movie(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        posterPath = it.posterPath,
                        liked = false
                    )
                }
            }
        }
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        return Result.of { mapper.mapFromLocal(localStore.getMovie(id)) }
    }

    override fun observeLikedMovieIds(): Flow<List<Int>> {
        return localStore.observeLikedMoviesIds()
    }

    override suspend fun addMovieToFavorites(movieId: Int) {
        localStore.likeMovie(movieId)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        localStore.dislikeMovie(movieId)
    }

    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            restStore.getMovies().let {
                Timber.i("refresh movies: $it")
            }
        }
    }
}