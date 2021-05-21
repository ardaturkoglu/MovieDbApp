package com.example.moviedb.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentMovieListBinding
import com.example.moviedb.network.MovieApiStatus


class MovieListFragment : Fragment() {
    private var binding: FragmentMovieListBinding? = null
    var state: Parcelable? = null

    private val sharedViewModel: MovieViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.movies.value = sharedViewModel.topMovies.value
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        val fragmentBinding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        return fragmentBinding.root

    }

    override fun onResume() {
        super.onResume()

        sharedViewModel.isTopRated.value = false
        recyclerView = binding!!.recyclerView
        recyclerView.addOnScrollListener(this.onScrollListener)
        recyclerView.adapter = MovieAdapter(
            sharedViewModel.movies.value!!,sharedViewModel.isTopRated.value!!
        )
        recyclerView.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            movieListFragment = this@MovieListFragment
        }

        sharedViewModel.status.observe(viewLifecycleOwner, Observer {
            when(sharedViewModel.status.value)
            {
                MovieApiStatus.LOADING -> Toast.makeText(context,"Movies are loading.",Toast.LENGTH_SHORT).show()
                MovieApiStatus.ERROR -> Toast.makeText(context,"Cannot load movies.",Toast.LENGTH_SHORT).show()
                MovieApiStatus.DONE -> Toast.makeText(context,"Movies loaded successfully.",Toast.LENGTH_SHORT).show()
            }
        })

        binding!!.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sharedViewModel.searchQuery.value = query.toString()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Start filtering the list as user start entering the characters
                sharedViewModel.isSearching =true
                sharedViewModel.currentPage.value = 1
                sharedViewModel.searchQuery.value = p0.toString()
                sharedViewModel.getMovies(sharedViewModel.searchQuery.value.toString(),
                    sharedViewModel.currentPage.value!!
                )
                sharedViewModel.isSearching =false

                return true
            }
        })
        sharedViewModel.movies.observe(viewLifecycleOwner, Observer {
            (recyclerView.adapter as MovieAdapter).updateList(sharedViewModel.movies.value!!)
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
            val layoutManager = binding?.recyclerView?.layoutManager as LinearLayoutManager
            val sizeOfCurrentList = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val topPosition = layoutManager.findFirstVisibleItemPosition()

            val isEnd = topPosition + visibleItems >= sizeOfCurrentList
            val shouldPaginate = sharedViewModel.status.value != MovieApiStatus.LOADING  && isEnd && isScrolling
            if(shouldPaginate && (sharedViewModel.currentPage.value!! <= sharedViewModel.totalPage.value!!))
            {
                sharedViewModel.currentPage.value = sharedViewModel.currentPage.value?.plus(1)
                sharedViewModel.getMovies(sharedViewModel.searchQuery.value.toString(),
                    sharedViewModel.currentPage.value!!
                )

            }
        }
    }

    }


