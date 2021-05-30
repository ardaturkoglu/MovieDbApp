package com.example.moviedb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.queryDb.QueryRepo
import java.lang.IllegalArgumentException

class MovieViewModelFactory(private val repository: QueryRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieViewModel::class.java)){
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}