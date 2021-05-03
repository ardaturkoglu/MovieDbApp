package com.example.moviedb.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.MovieItemBinding
import com.example.moviedb.network.MovieInfo

class MovieAdapter(val movies:List<MovieInfo>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    /**
     * Provides a reference for the views needed to display items in your list.
     */

    class MovieViewHolder( var binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(movieInfo: MovieInfo) {
                binding.movieInfo= movieInfo
                // This is important, because it forces the data binding to execute immediately,
                // which allows the RecyclerView to make the correct view size measurements
                binding.executePendingBindings()
            }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // Setup custom accessibility delegate to set the text read
        return MovieViewHolder(
            MovieItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie_item = movies[position] //movies[position]
        holder.bind(movie_item)
        // Assigns a [OnClickListener] to the button contained in the [ViewHolder]
        holder.binding.movieItem.setOnClickListener {
            // Create an action from WordList to DetailList
            // using the required arguments
            val action =
                MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie = movie_item.original_title.orEmpty(),id = movie_item.id)
            // Navigate using that action
            findNavController(it).navigate(action)

        }
    }

}