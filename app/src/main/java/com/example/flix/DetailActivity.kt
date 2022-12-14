package com.example.flix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

private const val YOUTUBE_API_KEY = "AIzaSyCFT6sQkDCXOicu7gRKPdtWgSjYPlOxiUk"
private const val TRAILERS_URL = "https://api.themoviedb" +
        ".org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

private const val TAG = "DetailActivity"

class DetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView
    private lateinit var tvReleaseDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val explode = Explode()

        window.enterTransition = explode
        window.exitTransition = explode

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        ratingBar = findViewById(R.id.rbVoteAverage)
        tvReleaseDate = findViewById(R.id.tvPremiereDate)

        ytPlayerView= findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        Log.i(TAG, "Movie is $movie")

        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        tvReleaseDate.text = "Release date: " + movie.release_date

        //TODO: get rating of the movie
        ratingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId),object:JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0){
                    Log.w(TAG, "No movie trailers found!")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")

                //play youtube video with this trailer
                initializeYoutube(youtubeKey)
            }

        })

    }

    private fun initializeYoutube(youtubeKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY,object:OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG,"onInitializationSuccess")
                // do any work here to cue video, play video, etc.
                player?.cueVideo(youtubeKey);
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG,"onInitializationFailure")
            }

        })
    }
}