package com.example.moviedb.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentErrorBinding
import com.example.moviedb.databinding.FragmentMovieDetailBinding
import com.example.moviedb.network.MovieApiStatus

class ErrorFragment : Fragment() {

    private var binding: FragmentErrorBinding? = null // Binding object of the movie detail fragment.
    private val sharedViewModel: MovieViewModel by activityViewModels() //ViewModel of the ui.

    //val path_arg : MovieDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedViewModel.status.value = MovieApiStatus.LOADING
    }
    /*
    * Init binding and layout.
    *
    * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentErrorBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            errorFragment = this@ErrorFragment
        }


        return fragmentBinding.root
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

    }

}