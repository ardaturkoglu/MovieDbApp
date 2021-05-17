package com.example.moviedb
import androidx.recyclerview.widget.DiffUtil
import com.example.moviedb.network.MovieInfo

class MovieDiffUtil (
    private val oldMovies: List<MovieInfo>,
    private val newMovies: List<MovieInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldMovies.size

    override fun getNewListSize(): Int = newMovies.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMovies[oldItemPosition].id == newMovies[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMovies[oldItemPosition] == newMovies[newItemPosition]
    }
}