package com.example.moviedb.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.FragmentTopRatedBinding
import com.example.moviedb.network.MovieApiStatus
import com.example.moviedb.queryDb.QueryDatabase
import com.example.moviedb.queryDb.QueryItem
import com.example.moviedb.queryDb.QueryRepo
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_top_rated.*

/**
 * A simple [Fragment] subclass.
 * Use the [TopRated.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopRatedFragment : Fragment() {
    private var binding: FragmentTopRatedBinding? = null

    private lateinit var sharedViewModel:MovieViewModel
    private var isScrolling = false

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val dao = QueryDatabase.getInstance(requireContext()).queryDAO
        val repository = QueryRepo(dao)
        val factory = MovieViewModelFactory(repository)
        sharedViewModel = ViewModelProvider(requireActivity(),factory).get(MovieViewModel::class.java)

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
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
             topRated= this@TopRatedFragment
        }
        //sharedViewModel.clearAll()
        sharedViewModel.getAll()

       /* sharedViewModel.isNight.observe(viewLifecycleOwner,{
            if(sharedViewModel.isNight.value == true)
                binding!!.searchRecycler.setBackgroundColor(Color.BLACK)
            else if (sharedViewModel.isNight.value == false)
                binding!!.searchRecycler.setBackgroundColor(Color.WHITE)
        })*/


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

        sharedViewModel.recents.observe(viewLifecycleOwner,{
            search_recycler.adapter = QueryAdapter(sharedViewModel.recents.value!!)
        })
        return fragmentBinding.root

    }

    //Update UI when fragment is visible.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView : SearchView? = binding?.button
        recyclerView = binding!!.recyclerView
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding!!.searchRecycler.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL))


       // if(sharedViewModel.isTopRated && savedInstanceState==null)
            //sharedViewModel.getTopRated(sharedViewModel.ratedCurrentPage.value!!)


        recyclerView.addOnScrollListener(this.onScrollListener) //Checks scroll position for paging.
        recyclerView.adapter = MovieAdapter(
            sharedViewModel.movies.value!!, sharedViewModel.isTopRated
        )
        recyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        binding!!.textView3.text = sharedViewModel.text
        searchView?.setOnSearchClickListener {
            //binding!!.textView3.visibility = INVISIBLE
            sharedViewModel.text = "Searching:"
        }

    }

    override fun onResume() {
        super.onResume()
        binding?.clearButton?.setOnClickListener { sharedViewModel.clearAll() }
        binding!!.button.setOnSearchClickListener { binding!!.searchRecycler.visibility = VISIBLE
        binding!!.clearButton.visibility = VISIBLE}
        binding!!.button.setOnCloseListener( object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                binding!!.searchRecycler.visibility = GONE
                binding!!.clearButton.visibility = GONE
                return true
            }
        })
        binding!!.button.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                sharedViewModel.searchQuery.value = query.toString()
                if (query != null && query != "") {
                    val newQuery = QueryItem(0,query)
                    sharedViewModel.insert(newQuery)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Start filtering the list as user start entering the characters
                sharedViewModel.text="Searching:"
                binding!!.searchRecycler.visibility = VISIBLE
                binding!!.clearButton.visibility = VISIBLE
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