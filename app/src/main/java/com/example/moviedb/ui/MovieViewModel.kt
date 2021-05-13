package com.example.moviedb.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.network.MovieApi
import com.example.moviedb.network.MovieDetail
import com.example.moviedb.network.MovieInfo
import kotlinx.coroutines.launch

enum class MovieApiStatus
{
    ERROR,LOADING,DONE,DETAIL_LOADED,DETAIL_ERROR,DETAIL_LOADING
}
class MovieViewModel : ViewModel() {
    // Internally, we use a MutableLiveData, because we will be updating the List of Movies
    // with new values
    val movies = MutableLiveData<List<MovieInfo>>()



    val searchQuery = MutableLiveData<String>()
    val movie_detail = MutableLiveData<MovieDetail>()
    val currentId = MutableLiveData<Int>()
    val status = MutableLiveData<MovieApiStatus>()


    fun getMovies(query: String) {
        viewModelScope.launch {
            status.value = MovieApiStatus.LOADING
            try {
                movies.value = MovieApi.retrofitService.getMovies(query).results
                status.value = MovieApiStatus.DONE
            } catch (e: Exception) {
                movies.value = listOf()
                status.value = MovieApiStatus.ERROR
                Log.d("ardaError",e.toString())
            }
        }

    }
    fun showMovieDetail(query: String) {
        viewModelScope.launch {
            status.value = MovieApiStatus.DETAIL_LOADING
            try {
                movie_detail.value = MovieApi.retrofitService.showMovieDetail(query)
                status.value = MovieApiStatus.DETAIL_LOADED
            } catch (e: Exception) {
                movie_detail.value = null
                status.value = MovieApiStatus.DETAIL_ERROR
                Log.d("ardaError",e.toString())
            }
        }
    }
}