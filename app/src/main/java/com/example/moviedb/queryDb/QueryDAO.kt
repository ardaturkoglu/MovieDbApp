package com.example.moviedb.queryDb

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface QueryDAO {

    @Insert
    suspend fun insertQuery(query:QueryItem):Long
    @Update
    suspend fun updateQuery(query: QueryItem)
    @Delete
    suspend fun deleteQuery(query:QueryItem)

    @Query("DELETE FROM query_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM (SELECT * FROM query_table ORDER BY qid ASC LIMIT 10) ORDER BY qid DESC")
    fun getLastQueries(): LiveData<List<QueryItem>>

    @Query("SELECT * FROM query_table")
    fun getAllQueries(): LiveData<List<QueryItem>>

}