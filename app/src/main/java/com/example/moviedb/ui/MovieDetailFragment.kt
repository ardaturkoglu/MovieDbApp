package com.example.moviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moviedb.databinding.FragmentMovieDetailBinding
import com.example.moviedb.network.MovieApi


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            movieDetailFragment = this@MovieDetailFragment
        }
        sharedViewModel.showMovieDetail("$25?/api_key=f019202a38bce5675c2660882dd6669c")
        binding?.movieDetail=sharedViewModel.movie_detail.value

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