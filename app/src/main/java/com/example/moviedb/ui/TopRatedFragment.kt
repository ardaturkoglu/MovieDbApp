package com.example.moviedb.ui

import android.os.Bundle
import android.os.Debug
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.databinding.FragmentTopRatedBinding
import com.example.moviedb.network.MovieApiStatus

/**
 * A simple [Fragment] subclass.
 * Use the [TopRated.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopRatedFragment : Fragment() {
    private var binding: FragmentTopRatedBinding? = null

    private val sharedViewModel: MovieViewModel by activityViewModels()
    private var isScrolling = false

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        val fragmentBinding = FragmentTopRatedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root

    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.isTopRated.value = true
        binding?.button?.setOnClickListener{
            val action =
                TopRatedFragmentDirections.actionTopRatedToMovieListFragment()
            // Navigate using that action
            Navigation.findNavController(it).navigate(action)
        }
        recyclerView = binding!!.recyclerView
        recyclerView.addOnScrollListener(this.onScrollListener)
        recyclerView.adapter = MovieAdapter(
            sharedViewModel.topMovies.value!!,sharedViewModel.isTopRated.value!!
        )
        recyclerView.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            topRated = this@TopRatedFragment
        }
        sharedViewModel.getTopRated(sharedViewModel.ratedCurrentPage.value!!)

//        sharedViewModel.status.observe(viewLifecycleOwner, Observer {
//            when(sharedViewModel.status.value)
//            {
//                MovieApiStatus.LOADING -> Toast.makeText(context,"Movies are loading...", Toast.LENGTH_SHORT).show()
//                MovieApiStatus.ERROR -> Toast.makeText(context,"Cannot load movies.", Toast.LENGTH_SHORT).show()
//                MovieApiStatus.DONE -> Toast.makeText(context,"Movies loaded successfully.", Toast.LENGTH_SHORT).show()
//            }
//        })

        sharedViewModel.topMovies.observe(viewLifecycleOwner, {
            (recyclerView.adapter as MovieAdapter).updateList(sharedViewModel.topMovies.value!!)
        })
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

    }
    private val onScrollListener =object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding?.recyclerView?.layoutManager as GridLayoutManager
            val sizeOfCurrentList = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val topPosition = layoutManager.findFirstVisibleItemPosition()

            val isEnd = topPosition + visibleItems >= sizeOfCurrentList
            val shouldPaginate = isEnd && isScrolling
            if(shouldPaginate && (sharedViewModel.ratedCurrentPage.value!! <= sharedViewModel.ratedTotalPage.value!!))
            {
                sharedViewModel.ratedCurrentPage.value = sharedViewModel.ratedCurrentPage.value?.plus(1)
                sharedViewModel.getTopRated(
                    sharedViewModel.ratedCurrentPage.value!!
                )

            }
        }
    }

}