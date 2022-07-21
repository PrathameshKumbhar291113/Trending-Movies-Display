package com.example.moviedisplayapp.api

import com.example.moviedisplayapp.Constants
import com.example.moviedisplayapp.models.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
//    Interface to get the data from the api
    @GET(TRENDING_MOVIES)
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY
    ) : MoviesResponse
    // path param of api - as we want trending + movie + week : in short weekly trending movies
    companion object {
        const val TRENDING_MOVIES = "3/trending/movie/week"
    }
}