package com.example.moviedb.ui
import androidx.lifecycle.*
import com.example.moviedb.network.MovieApi.retrofitService
import com.example.moviedb.network.MovieDbApi
import com.example.moviedb.network.MovieInfo
import kotlinx.coroutines.launch

class MovieViewModel: ViewModel() {
    // Internally, we use a MutableLiveData, because we will be updating the List of Movies
    // with new values
    private val _movies = MutableLiveData<List<MovieInfo>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val movies: LiveData<List<MovieInfo>> = _movies

    /**
     * Call getMovies() on init so we can display status immediately.
     */
    init {
        getMovies()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MovieInfo] [List] [LiveData].
     */
    private fun getMovies() {

        viewModelScope.launch {
            try {
                _movies.value = MovieDbApi.retrofitService.getMoviesInfo()
            } catch (e: Exception) {
                _movies.value = listOf()
            }
        }
    }
}