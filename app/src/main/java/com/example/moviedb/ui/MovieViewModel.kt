package com.example.moviedb.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.network.MovieApi
import com.example.moviedb.network.MovieApiStatus
import com.example.moviedb.network.MovieDetail
import com.example.moviedb.network.MovieInfo
import kotlinx.coroutines.launch


class MovieViewModel : ViewModel() {
    // Internally, we use a MutableLiveData, because we will be updating the List of Movies
    // with new values
    val movies = MutableLiveData<List<MovieInfo>>(listOf())
    val topMovies = MutableLiveData<List<MovieInfo>>(listOf())
    val searchQuery = MutableLiveData<String>()
    val movie_detail = MutableLiveData<MovieDetail>()
    val totalPage = MutableLiveData<Int>()
    val ratedTotalPage = MutableLiveData<Int>()
    val status = MutableLiveData<MovieApiStatus>()
    val currentPage = MutableLiveData<Int>(1)
    val ratedCurrentPage = MutableLiveData<Int>(1)
    val isTopRated = MutableLiveData<Boolean>( false)
    var isSearching = false



    fun getMovies(query: String,page:Int) : List<MovieInfo>? {
        viewModelScope.launch {
            status.value = MovieApiStatus.LOADING
            try {
                if(isSearching) {
                    movies.value = MovieApi.retrofitService.getMovies(query, page).results
                }
                else {
                    movies.value =
                        movies.value?.plus(MovieApi.retrofitService.getMovies(query, page).results)
                }
                totalPage.value = MovieApi.retrofitService.getMovies(query,page).pageTotal
                status.value = MovieApiStatus.DONE
            } catch (e: Exception) {
                movies.value = listOf()
                status.value = MovieApiStatus.ERROR
            }
        }
        return movies.value

    }

    fun getTopRated(page:Int) : List<MovieInfo>? {
        viewModelScope.launch {
            status.value = MovieApiStatus.LOADING
            try {
                topMovies.value =
                    topMovies.value?.plus(MovieApi.retrofitService.getTopRated(page).results)
                ratedTotalPage.value = MovieApi.retrofitService.getTopRated(page).pageTotal
                status.value = MovieApiStatus.DONE
            } catch (e: Exception) {
                topMovies.value = listOf()
                status.value = MovieApiStatus.ERROR
            }
        }
        return topMovies.value

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