package com.example.flix

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.text.SimpleDateFormat


const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"

class MovieAdapter(private val context: Context, private val movies: List<Movie>) : RecyclerView
.Adapter<MovieAdapter.ViewHolder>() {


    // Expensive operation: create a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        private val tvPremiereDate = itemView.findViewById<TextView>(R.id.tvPremiereDate)



        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            tvPremiereDate.text = "Release date: " + movie.release_date

            val radius = 30 // corner radius, higher value = more rounded
            val margin = 10 // crop margin, set to 0 for corners with no crop


            Glide.with(context)
                .load(movie.posterImageUrl)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin,
                    RoundedCornersTransformation.CornerType.ALL)))
                .placeholder(R
                .drawable
                    .poster_placeholder)
                .into(ivPoster)

        }

        override fun onClick(p0: View?) {
            // 1. Get notified of the movie that was clicked

            val movie = movies[adapterPosition]
//            Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()

            // 2. Open an intent to show its details

            val intent = Intent(context, DetailActivity::class.java)




            // on below line we are creating a variable
            // for activity options compact and setting
            // transition for our activity.
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity, ivPoster, ViewCompat.getTransitionName(ivPoster)!!
            )


            intent.putExtra(MOVIE_EXTRA, movie)

            context.startActivity(intent,options.toBundle())

        }
    }
}
