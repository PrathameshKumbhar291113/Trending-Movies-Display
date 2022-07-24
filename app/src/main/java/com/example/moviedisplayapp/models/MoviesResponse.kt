package com.example.moviedisplayapp.models

import androidx.annotation.Keep
//By using the JSON to kotlin data class plugin converted JSON to the dataClass
@Keep
data class MoviesResponse(
    var page: Int? = null,
    var results: List<ResultMovies>? = null,
    var total_pages: Int? = null,
    var total_results: Int? = null
)