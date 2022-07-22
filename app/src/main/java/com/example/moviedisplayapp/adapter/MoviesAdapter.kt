package com.example.moviedisplayapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviedisplayapp.databinding.ItemMoviesBinding
import com.example.moviedisplayapp.models.Result
// Movies Adapter is the Adapter for the recycler view, we have use the view binding to bind views
// and to bind image in the card view
class MoviesAdapter(
    val onClick: (Result) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {
// Diff util is used to check the difference between the current list and the previous list
    private val diffCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
// inflating the item into the adapter (viewHolder)
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MoviesViewHolder {
        return MoviesViewHolder(
            ItemMoviesBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
//returning the size of the current list
    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(
        holder: MoviesViewHolder, position: Int
    )  = holder.bind(differ.currentList[position])

    inner class MoviesViewHolder(
        val binding: ItemMoviesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        //function to bind ViewHolder's UI with ItemPosition's Data
        fun bind(result: Result) {
            //load image for Movie poster as we are only getting the image path from the api we are
            // using coil library to load the image from the given poster path
            binding.moviePoster.apply {
                load(
                    "https://image.tmdb.org/t/p/w500" + result.poster_path
                ) {
                    crossfade(true)
                }
                setOnClickListener { onClick(result) }
            }
        }
    }
}