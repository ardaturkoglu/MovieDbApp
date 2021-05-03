package com.example.moviedb.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.network.MovieApi
import com.example.moviedb.network.MovieDetail
import com.example.moviedb.network.MovieInfo
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    // Internally, we use a MutableLiveData, because we will be updating the List of Movies
    // with new values
    val movies = MutableLiveData<List<MovieInfo>>()



    val searchQuery = MutableLiveData<String>()
    val movie_detail = MutableLiveData<MovieDetail>()
    val currentId = MutableLiveData<Int>()
    init{
        //getMovies(searchQuery.value.toString())
    }

    fun getMovies(query: String) {
        viewModelScope.launch {
            try {
                movies.value = MovieApi.retrofitService.getMovies(query).results
            } catch (e: Exception) {
                movies.value = listOf()
            }
        }

    }
    fun showMovieDetail(query: String) {
        viewModelScope.launch {
            try {
                movie_detail.value = MovieApi.retrofitService.showMovieDetail(query)
                Log.d("list","detailTextinfun:$query")
                Log.d("list","movieListinfun:${movie_detail.value}")
            } catch (e: Exception) {
                movie_detail.value = null
            }
        }
    }
}