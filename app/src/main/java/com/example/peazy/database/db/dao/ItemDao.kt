package com.example.peazy.database.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.peazy.models.menu_item.Item

interface ItemDao {
    @Insert
    suspend fun insertItem(item: Item): Long

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    /* @Query("Delete from Item")
     suspend fun deleteAll()

     @Query("select * from Item")
     fun getAllItem(): LiveData<List<Item>>*/
}