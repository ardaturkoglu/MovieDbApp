package com.example.moviedb.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.network.MovieApi
import com.example.moviedb.network.MovieApiStatus
import com.example.moviedb.network.MovieDetail
import com.example.moviedb.network.MovieInfo
import com.example.moviedb.queryDb.QueryItem
import com.example.moviedb.queryDb.QueryRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import android.util.Patterns
import androidx.lifecycle.*


class MovieViewModel(private val repository: QueryRepo) : ViewModel() {
    // Internally, we use a MutableLiveData, because we will be updating the List of Movies
    // with new values
    val movies = MutableLiveData<List<MovieInfo>>(listOf()) //Movie list for search.
    var searchQuery = MutableLiveData<String>() //Search query
    val movie_detail = MutableLiveData<MovieDetail?>() //Movie Detail info
    val totalPage = MutableLiveData<Int>() //Total page number of the searched movies.
    val status = MutableLiveData<MovieApiStatus>(MovieApiStatus.LOADING)//Status of the API
    val currentPage = MutableLiveData<Int>(1) // current shown page for the searched movie list.
    val ratedCurrentPage = MutableLiveData<Int>(1) // current shown page for the top rated movie list.
    var isTopRated =  true //True if user is looking to top rated movies.
    var isSearching = false //Is search bar changing?
    var text ="Top Rated Movies:"
    var isNight = MutableLiveData(false)
    lateinit var recents :LiveData<List<QueryItem>>

    init{
        getTopRated(ratedCurrentPage.value!!)
    }

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

    fun insert(query: QueryItem)= viewModelScope.launch {
        repository.insert(query)
    }

    fun update(query: QueryItem) = viewModelScope.launch {
        repository.update(query)
    }

    fun delete(query: QueryItem) = viewModelScope.launch {
        repository.delete(query)

    }

    fun clearAll()=viewModelScope.launch {
        repository.deleteAll()
    }
    fun getAll() = viewModelScope.launch {
        recents = repository.queries
    }
    fun getRecent()=viewModelScope.launch {
        recents = repository.lastQueries
        }
}