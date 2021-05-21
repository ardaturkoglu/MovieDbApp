package com.example.moviedb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.MovieDiffUtil
import com.example.moviedb.databinding.MovieItemBinding
import com.example.moviedb.network.MovieInfo

class MovieAdapter(var movies:List<MovieInfo>,var isTopRated:Boolean) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    /**
     * updateList to show in recyclerview.
     */
    fun updateList(newMovies: List<MovieInfo>) {
        val diffCallBack = MovieDiffUtil(movies, newMovies)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        diffResult.dispatchUpdatesTo(this)

        movies = newMovies
    }
    class MovieViewHolder( var binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(movieInfo: MovieInfo) {
                binding.movieInfo= movieInfo
                binding.imageButton.transitionName ="${binding.movieInfo!!.poster_path}"
                Glide.with(binding.root).load("https://image.tmdb.org/t/p/original/${binding.movieInfo!!.poster_path}").into(binding.imageButton)
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
            val action: NavDirections?
            val extras = FragmentNavigatorExtras(
                holder.binding.imageButton to "${holder.binding.movieInfo!!.poster_path}"
            )
            // Create an action from WordList or TopRated to DetailList
            // using the required arguments
            if(isTopRated)
            {
                 action =
                    TopRatedFragmentDirections.actionTopRatedToMovieDetailFragment(movie = movie_item.original_title.orEmpty(),id = movie_item.id,moviePath = "${holder.binding.movieInfo!!.poster_path}")
            }
            else
            {
                 action =
                    MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie = movie_item.original_title.orEmpty(),id = movie_item.id,moviePath ="${holder.binding.movieInfo!!.poster_path}")
            }

            // Navigate using that action
            findNavController(it).navigate(action,extras)

        }
    }

}