package com.example.moviedb.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentMovieDetailBinding
import com.example.moviedb.network.MovieApiStatus
import com.example.moviedb.queryDb.QueryDatabase
import com.example.moviedb.queryDb.QueryRepo


/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {

    private var binding: FragmentMovieDetailBinding? = null // Binding object of the movie detail fragment.
    private lateinit var sharedViewModel: MovieViewModel //ViewModel of the ui.
    var genreList: String? =""
    val path_arg : MovieDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val dao = QueryDatabase.getInstance(requireContext()).queryDAO
        val repository = QueryRepo(dao)
        val factory = MovieViewModelFactory(repository)
        sharedViewModel = ViewModelProvider(requireActivity(),factory).get(MovieViewModel::class.java)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
}
    /*
    * Init binding and layout.
    *
    * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            movieDetailFragment = this@MovieDetailFragment
        }

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (arguments != null) {
            val currentId: Int = MovieDetailFragmentArgs.fromBundle(requireArguments()).id
            val imagePath :String = path_arg.moviePath
            sharedViewModel.showMovieDetail(currentId.toString()) //Get movie details of the clicked movie.
            binding?.movieImage?.transitionName  = imagePath
            Glide.with(binding!!.root).load("https://image.tmdb.org/t/p/original/${imagePath}").apply(RequestOptions.centerInsideTransform()
                ).into(
               binding!!.movieImage)
        }

        //Update UI with movie detail.
        sharedViewModel.movie_detail.observe(viewLifecycleOwner, Observer {
            binding?.movieDetail = sharedViewModel.movie_detail.value

            sharedViewModel.movie_detail.value?.genres?.forEach { genreList+= it.genre_name + " " }
            binding?.overviewText2?.text=genreList
            genreList = ""
        })

        //Update Status
        sharedViewModel.status.observe(viewLifecycleOwner, Observer {
            when(sharedViewModel.status.value)
            {


                MovieApiStatus.DETAIL_LOADING -> Toast.makeText(context,"Movie detail is loading...", Toast.LENGTH_SHORT).show()
                MovieApiStatus.DETAIL_ERROR -> Navigation.findNavController(binding!!.root).navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToErrorFragment())
                MovieApiStatus.DETAIL_LOADED -> Toast.makeText(context,"Movie detail loaded successfully.", Toast.LENGTH_SHORT).show()
            }
        })


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