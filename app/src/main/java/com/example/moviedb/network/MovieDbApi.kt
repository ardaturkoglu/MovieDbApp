package com.example.moviedb.network
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"

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

    @GET("search/movie?api_key=f019202a38bce5675c2660882dd6669c&include_adult=false")
    suspend fun getMovies(@Query("query") query: String,@Query("page") page:Int):Base //

    @GET("movie/top_rated?api_key=f019202a38bce5675c2660882dd6669c")
    suspend fun getTopRated(@Query("page") page:Int):Base //

    @GET("movie/{id}?api_key=f019202a38bce5675c2660882dd6669c")
    suspend fun showMovieDetail(@Path("id") id: String):MovieDetail
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MovieApi {
    val retrofitService: MovieDbApi by lazy { retrofit.create(MovieDbApi::class.java) }
}
enum class MovieApiStatus
{
    ERROR,LOADING,DONE,DETAIL_LOADED,DETAIL_ERROR,DETAIL_LOADING
}