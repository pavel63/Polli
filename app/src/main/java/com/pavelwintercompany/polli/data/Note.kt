package com.pavelwintercompany.polli.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "hash")  val hash : String?,
    /** 1-выше приоритет, 2-меньше и тд */
    @ColumnInfo(name = "rating") val rating: Int?,
    @ColumnInfo(name = "question") val question: String?,
    @ColumnInfo(name = "answer") val answer: String?
)