package com.example.moviedisplayapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedisplayapp.adapter.MoviesAdapter
import com.example.moviedisplayapp.api.ApiService
import com.example.moviedisplayapp.databinding.ActivityHomeBinding
import com.example.moviedisplayapp.models.ResultMovies
import com.example.moviedisplayapp.utils.DefaultPagingSource
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.snackbar.snack
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeActivity : AppCompatActivity() {
    //Here we have used the view binding to bind the views to set the data inside views

    private lateinit var adapter: MoviesAdapter
    private lateinit var binding: ActivityHomeBinding
    private var currKey = 1
    private var moviesList = mutableListOf<ResultMovies>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inflate the layout & get binding instance
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup the RecyclerView first when screen loads
        setupRecyclerView()

        //Finally we will call our api to fetch our data
        lifecycleScope.launch {
            moviesPagingSource.loadNext()
        }
    }
//creating the intent and sending the data getting from the api in key pair values
//    used splitties library to send the intent from this activity to the other
    private fun setupRecyclerView() {
        adapter = MoviesAdapter { result ->
            start<MovieDetailActivity>(){
                putExtra("movie",result)
            }
        }
        binding.moviesListRv.adapter = adapter

        binding.moviesListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val layoutManager = binding.moviesListRv.layoutManager as LinearLayoutManager
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Timber.w("Scrolled")
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPosition()
                // When last item in user's display is before PRE_LOAD_SIZE load next batch
                // This is done to enable continuous scrolling.
                // If PRE_LOAD_SIZE is set too small; user encounters temporary end. This occurs because
                // next items are not yet loaded in RecyclerView
                if (lastVisibleItemPos >= moviesList.lastIndex - 8) {
                    lifecycleScope.launch {
                        moviesPagingSource.loadNext()
                    }
                }
            }
        })
    }

    private var moviesPagingSource = DefaultPagingSource<Int,ResultMovies>(
        initialKey = 1,
        requestedItems = {key ->
            try{
                val movies = ApiService.restService.getTrendingMovies(pageNum = key)
                movies.results ?: emptyList()
            }catch (e : Exception){
                e.printStackTrace()
                emptyList()
            }
        },
        onSuccess = { newlist, nextkey ->
            moviesList.addAll(newlist)
            adapter.differ.submitList(moviesList)
            Timber.i(newlist.toPrettyString())
        },
        onError = { e ->
            with(binding.root) {
                when(e) {
                    is ConnectException -> snack("No Internet Connection Available!")
                    is UnknownHostException -> snack("You connection was reset")
                    is SocketTimeoutException -> snack("Failed to get Movies. Try Again")
                    else -> snack("Something went wrong, Please Try Again")
                }
            }
        },
        getNextKey = {
            currKey++
        }
    )
// created the function to get all result(movies) from the api
//    suspend fun getTrendingMovies() {
//        try {
//            val movies = ApiService.restService.getTrendingMovies(/*NEXT_PAGE*/)
//            if(!movies.results.isNullOrEmpty()) {
////                var pageNumber =1
////
//                    adapter.differ.submitList(movies.results)
////                    pageNumber++
////                }
//
//            } else {
//                binding.root.snack("No movies are Trending! Everyone is watching Netflix")
//            }
//        } catch (e: Exception) {
//            with(binding.root) {
//                when(e) {
//                    is ConnectException -> snack("No Internet Connection Available!")
//                    is UnknownHostException -> snack("You connection was reset")
//                    is SocketTimeoutException -> snack("Failed to get Movies. Try Again")
//                    else -> snack("Something went wrong, Please Try Again")
//                }
//            }
//        }
//    }
}

fun Collection<ResultMovies>.toPrettyString() : String {
    return buildString {
        this@toPrettyString.forEach {
            append(it.title + "\n")
        }
    }
}