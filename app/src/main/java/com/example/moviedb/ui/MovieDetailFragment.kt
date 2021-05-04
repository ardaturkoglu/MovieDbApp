package com.example.moviedb.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.moviedb.databinding.FragmentMovieDetailBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {

    // Binding object instance corresponding to the fragment_flavor.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentMovieDetailBinding? = null
    private val sharedViewModel: MovieViewModel by activityViewModels()
    var genreList: String? =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        if (arguments != null) {
            val currentId: Int = MovieDetailFragmentArgs.fromBundle(requireArguments()).id
            sharedViewModel.showMovieDetail(currentId.toString())
        }
        sharedViewModel.movie_detail.value?.genres?.forEach { genreList+= it.genre_name + " ," }
        sharedViewModel.movie_detail.observe(viewLifecycleOwner, Observer {
            binding?.movieDetail = sharedViewModel.movie_detail.value
            Glide.with(binding!!.root).load("https://image.tmdb.org/t/p/original/${sharedViewModel.movie_detail.value?.poster_path}").into(
                binding!!.movieImage)
            binding?.overviewText2?.text=genreList
        })

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            movieDetailFragment = this@MovieDetailFragment
        }


    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}