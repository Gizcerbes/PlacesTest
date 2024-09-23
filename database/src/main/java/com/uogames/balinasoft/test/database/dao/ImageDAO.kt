package com.uogames.balinasoft.test.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.uogames.balinasoft.test.database.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDAO {

    @Upsert
    suspend fun save(vararg entities: ImageEntity)

    @Delete
    suspend fun delete(entity: ImageEntity)

    @Query("DELETE FROM image_table")
    suspend fun clear()

    @Query("DELETE FROM image_table WHERE id = :id")
    suspend fun deleteByID(id:Int)

    @Query("DELETE FROM image_table WHERE id  NOT IN (:list) AND page = :page")
    suspend fun deleteInsteadOnPage(list: List<Int>, page: Int)

    @Query("DELETE FROM image_table WHERE id = :page")
    suspend fun deleteOnPage(page: Int)

    @Query("SELECT * FROM image_table")
    fun getListFlow(): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image_table WHERE id = :id")
    suspend fun getByID(id: Int): ImageEntity?

    @Query("SELECT COUNT(id) FROM comment_table")
    suspend fun getSize(): Int

}