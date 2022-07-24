package com.example.moviedisplayapp.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
//By using the JSON to kotlin data class plugin converted JSON to the dataClass
@Keep
@Parcelize
data class ResultMovies(
    var adult: Boolean? = null,
    var backdrop_path: String? = null,
    var genre_ids: List<Int>? = null,
    var id: Int? = null,
    var media_type: String? = null,
    var original_language: String? = null,
    var original_title: String? = null,
    var overview: String? = null,
    var popularity: Double? = null,
    var poster_path: String? = null,
    var release_date: String? = null,
    var title: String? = null,
    var video: Boolean? = null,
    var vote_average: Double? = null,
    var vote_count: Int? = null
) : Parcelable // Used parcelable to send the data in form of the bundles.