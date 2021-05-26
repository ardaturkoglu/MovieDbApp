package com.example.moviedb.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
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
    val movies = MutableLiveData<List<MovieInfo>>(listOf()) //Movie list for search.
    val searchQuery = MutableLiveData<String>() //Search query
    val movie_detail = MutableLiveData<MovieDetail>() //Movie Detail info
    val totalPage = MutableLiveData<Int>() //Total page number of the searched movies.
    val status = MutableLiveData<MovieApiStatus>()//Status of the API
    val currentPage = MutableLiveData<Int>(1) // current shown page for the searched movie list.
    val ratedCurrentPage = MutableLiveData<Int>(1) // current shown page for the top rated movie list.
    var isTopRated =  true //True if user is looking to top rated movies.
    var isSearching = false //Is search bar changing?
    var text ="Top Rated Movies:"


/*
* Get movies from the API according to search query and current page.
* */
    fun getMovies(query: String,page:Int) : List<MovieInfo>? {
        viewModelScope.launch {
            if(query.isNotEmpty()) {
                status.value = MovieApiStatus.LOADING
                try {
                    //If user is not searching, do not update the movies; show the current movies.
                    if (isSearching) {
                        movies.value = MovieApi.retrofitService.getMovies(query, page).results
                    } else {
                        movies.value =
                            movies.value?.plus(
                                MovieApi.retrofitService.getMovies(
                                    query,
                                    page
                                ).results
                            )
                    }
                    totalPage.value = MovieApi.retrofitService.getMovies(query, page).pageTotal
                    status.value = MovieApiStatus.DONE
                } catch (e: Exception) {
                    //Show error message if request fails.
                    movies.value = listOf()
                    status.value = MovieApiStatus.ERROR
                }
            }
        }
        return movies.value

    }
    //Get top rated movies from the API.
    fun getTopRated(page:Int) : List<MovieInfo>? {
        viewModelScope.launch {
            status.value = MovieApiStatus.LOADING
            try {
                movies.value =
                    movies.value?.plus(MovieApi.retrofitService.getTopRated(page).results)
                totalPage.value = MovieApi.retrofitService.getTopRated(page).pageTotal
                status.value = MovieApiStatus.DONE
            } catch (e: Exception) {
                movies.value = listOf()
                status.value = MovieApiStatus.ERROR
            }
        }
        return movies.value

    }
    //Show details of the selected movie.
    fun showMovieDetail(query: String) {
        viewModelScope.launch {
            status.value = MovieApiStatus.DETAIL_LOADING
            try {
                movie_detail.value = MovieApi.retrofitService.showMovieDetail(query)
                status.value = MovieApiStatus.DETAIL_LOADED
            } catch (e: Exception) {
                movie_detail.value = null
                status.value = MovieApiStatus.DETAIL_ERROR
            }
        }
    }

}