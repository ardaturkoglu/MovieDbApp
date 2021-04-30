package com.example.moviedb.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R

class MovieAdapter :RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<LinearLayout>(R.id.movie_item)
    }

    override fun getItemCount(): Int {
        return 1
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.movie_item, parent, false)

        // Setup custom accessibility delegate to set the text read
        return MovieViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        holder.button.text = item.toString()

        // Assigns a [OnClickListener] to the button contained in the [ViewHolder]
        holder.button.setOnClickListener {
            // Create an action from WordList to DetailList
            // using the required arguments
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie = "movieExample")
            // Navigate using that action
            holder.view.findNavController().navigate(action)
            val tag = "MovieListFragment"
            Log.d(tag,"Entered movie details")
        }
    }

}