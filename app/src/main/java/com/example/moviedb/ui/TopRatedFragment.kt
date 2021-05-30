package com.example.moviedb.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentTopRatedBinding
import com.example.moviedb.network.MovieApiStatus
import com.example.moviedb.queryDb.QueryDatabase
import com.example.moviedb.queryDb.QueryRepo
import com.google.android.material.snackbar.Snackbar

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
        val dao = QueryDatabase.getInstance(requireContext()).queryDAO
        val repository = QueryRepo(dao)


    }

    //Init binding and layout of the fragment.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        val fragmentBinding = FragmentTopRatedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        sharedViewModel.status.observe(viewLifecycleOwner, Observer {
            when (sharedViewModel.status.value) {
                MovieApiStatus.LOADING -> Toast.makeText(
                    context,
                    "Movies are loading.",
                    Toast.LENGTH_SHORT
                ).show()
                MovieApiStatus.ERROR -> {
                    Navigation.findNavController(binding!!.root).navigate(TopRatedFragmentDirections.actionTopRatedToErrorFragment())
                }
                MovieApiStatus.DONE -> Toast.makeText(
                    context,
                    "Movies loaded successfully.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        sharedViewModel.movies.observe(viewLifecycleOwner, {
            (recyclerView.adapter as MovieAdapter).updateList(sharedViewModel.movies.value!!)
        })
        return fragmentBinding.root

    }

    //Update UI when fragment is visible.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("deneme","created")
        val searchView : SearchView? = binding?.button
        recyclerView = binding!!.recyclerView
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        if(sharedViewModel.isTopRated && savedInstanceState==null)
            sharedViewModel.getTopRated(sharedViewModel.ratedCurrentPage.value!!)
        recyclerView.addOnScrollListener(this.onScrollListener) //Checks scroll position for paging.
        recyclerView.adapter = MovieAdapter(
            sharedViewModel.movies.value!!, sharedViewModel.isTopRated
        )
        recyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            topRated = this@TopRatedFragment
        }
        binding!!.textView3.text = sharedViewModel.text
        searchView?.setOnSearchClickListener {
            //binding!!.textView3.visibility = INVISIBLE
            sharedViewModel.text = "Searching:"
        }

    }

    override fun onResume() {
        super.onResume()
        binding!!.button.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sharedViewModel.searchQuery.value = query.toString()
                if (query != null && query != "") {
                    //sharedViewModel.recents?.add(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Start filtering the list as user start entering the characters
                sharedViewModel.text="Searching:"
                binding!!.textView3.text=sharedViewModel.text
                sharedViewModel.isSearching = true
                sharedViewModel.isTopRated = false
                sharedViewModel.currentPage.value = 1
                sharedViewModel.searchQuery.value = p0.toString()
                sharedViewModel.getMovies(
                    sharedViewModel.searchQuery.value.toString(),
                    sharedViewModel.currentPage.value!!
                )
                sharedViewModel.isSearching = false

                return true
            }
        })


    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.d("deneme","destroyed")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
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
            val shouldPaginate =
                sharedViewModel.status.value != MovieApiStatus.LOADING && isEnd && isScrolling
            if (shouldPaginate && (sharedViewModel.currentPage.value!! <= sharedViewModel.totalPage.value!!)) {
                if(!sharedViewModel.isTopRated) {
                    sharedViewModel.currentPage.value = sharedViewModel.currentPage.value?.plus(1)
                    sharedViewModel.getMovies(
                        sharedViewModel.searchQuery.value.toString(),
                        sharedViewModel.currentPage.value!!
                    )
                }
                else {
                    sharedViewModel.currentPage.value = sharedViewModel.currentPage.value?.plus(1)
                    sharedViewModel.getTopRated(sharedViewModel.currentPage.value!!)
                }

            }
        }

    }
}