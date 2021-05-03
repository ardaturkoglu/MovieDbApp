package com.example.moviedb.network

import com.squareup.moshi.Json

data class MovieInfo (
        @Json(name ="poster_path") val poster_path : String?,
        @Json(name ="overview") val overview: String?,
        @Json(name ="genre_ids") val genre_ids:List<Int>?,
        @Json(name ="id") val id:Int,
        @Json(name ="original_title") val original_title:String?,
        @Json(name ="backdrop_path") val backdrop_path:String?,
        @Json(name ="vote_average") val vote_average:Float?
)
data class Base(
        @Json(name="results") val results:List<MovieInfo>,
        @Json(name="page") val page:Int
)
data class MovieDetail(
        @Json(name ="poster_path") val poster_path : String?,
        @Json(name ="overview") val overview: String?,
        @Json(name ="genres") val genre_ids:List<Any>?,
        @Json(name ="original_title") val original_title:String?,
        @Json(name ="backdrop_path") val backdrop_path:String?,
        @Json(name ="vote_average") val vote_average:Float?

)
data class ObjectName(
        @Json (name="object") val details:MovieDetail
)