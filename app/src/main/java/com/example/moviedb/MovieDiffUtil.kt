package com.example.moviedb
import androidx.recyclerview.widget.DiffUtil
import com.example.moviedb.network.MovieInfo

class MovieDiffUtil (
    private val oldNumbers: List<MovieInfo>,
    private val newNumbers: List<MovieInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldNumbers.size

    override fun getNewListSize(): Int = newNumbers.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNumbers[oldItemPosition] == newNumbers[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNumbers[oldItemPosition] == newNumbers[newItemPosition]
    }
}