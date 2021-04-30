package com.example.moviedb.network
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/search/movie?api_key=f019202a38bce5675c2660882dd6669c&language=en-US"
//private const val MOVIE_URL = "https://api.themoviedb.org/3/movie/${MovieInfo.movieId}?api_key=f019202a38bce5675c2660882dd6669c&language=en-US"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

/**
 * A public interface that exposes the [getMoviesInfo] method
 */
interface MovieDbApi {
    /**
     * Returns a [List] of [MovieInfo] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("/search/movie")
    suspend fun getMoviesInfo(@Query("api_key") apiKey:String, @Query("query")query:String): List<MovieInfo>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MovieApi {
    val retrofitService: MovieDbApi by lazy { retrofit.create(MovieDbApi::class.java) }
}
