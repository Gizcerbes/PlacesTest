package com.uogames.balinasoft.test.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.uogames.balinasoft.test.database.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDAO {


    @Upsert
    suspend fun save(vararg entities: CommentEntity)

    @Delete
    suspend fun delete(entity: CommentEntity)

    @Query("SELECT * FROM comment_table WHERE image_id = :imageID")
    fun getListFlow(imageID: Int): Flow<List<CommentEntity>>

    @Query("DELETE FROM comment_table WHERE image_id = :imageID")
    suspend fun deleteByImageID(imageID: Int)

    @Query("DELETE FROM comment_table WHERE id NOT IN (:list) AND page = :page AND image_id = :imageID")
    suspend fun deleteInsteadOnPageAndImageId(list: List<Int>, page:Int, imageID: Int)

    @Query("DELETE FROM comment_table WHERE id = :id")
    suspend fun deleteByID(id: Int)

    @Query("DELETE FROM comment_table WHERE page = :page")
    suspend fun deleteOnPage(page:Int)

    @Query("DELETE FROM comment_table")
    suspend fun clear()

    @Query("SELECT COUNT(id) FROM comment_table")
    suspend fun getSize(): Int




}