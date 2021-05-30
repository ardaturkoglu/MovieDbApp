package com.example.moviedb.queryDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "query_table")
data class QueryItem (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "qid")
    var qid : Int,

    @ColumnInfo(name = "query_name")
    val query_name : String
)