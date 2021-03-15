package com.pavelwintercompany.polli.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
        @Query("SELECT * FROM note")
        fun getAll(): List<Note>

        @Insert
        fun insertAll(vararg notes: Note)


        /*  @Query("SELECT * FROM user WHERE uid IN (:userIds)")
          fun loadAllByIds(userIds: IntArray): List<User>

          @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
                  "last_name LIKE :last LIMIT 1")
          fun findByName(first: String, last: String): User


          @Delete
          fun delete(user: User)*/
}