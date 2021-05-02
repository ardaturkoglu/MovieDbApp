package com.example.moviedb.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.network.MovieApi.retrofitService
import com.example.moviedb.network.MovieInfo
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    // Internally, we use a MutableLiveData, because we will be updating the List of Movies
    // with new values
    //ToDo: Update movies with data from api.
    private val _movies = MutableLiveData<List<MovieInfo>>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val movies: LiveData<List<MovieInfo>> = _movies

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery = _searchQuery

    /**
     * Call getMovies() on init so we can display status immediately.
     */
//    init {
//        getMovies()
//    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MovieInfo] [List] [LiveData].
     */
    private fun getMovies(apiKey: String, query: String) {

        viewModelScope.launch {
            try {
                _movies.value = retrofitService.getMovies(apiKey, query)
            } catch (e: Exception) {
                _movies.value = listOf()
            }
        }
    }
    private fun showMovie(movieId:Int)
    {
        //return MovieInfo
    }
}