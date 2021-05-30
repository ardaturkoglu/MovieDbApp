package com.example.moviedb.queryDb

class QueryRepo(private val dao : QueryDAO) {

   // var queries = dao.getAllQueries()
    val lastQueries = dao.getLastQueries()

    suspend fun insert(query: QueryItem) {
         dao.insertQuery(query)
    }
    suspend fun update(query: QueryItem) {
        dao.updateQuery(query)
    }
    suspend fun delete(query: QueryItem) {
         dao.deleteQuery(query)
    }

    suspend fun deleteAll() {
         dao.deleteAll()
    }

}