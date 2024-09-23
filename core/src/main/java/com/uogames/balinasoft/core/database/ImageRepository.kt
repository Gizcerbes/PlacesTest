package com.uogames.balinasoft.core.database

import com.uogames.balinasoft.core.model.image.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun saveImages(vararg image: Image)

    suspend fun delete(image: Image)

    suspend fun deleteByID(id: Int)

    suspend fun deleteInstead(ids: List<Int>, page: Int)

    suspend fun deleteOnPage(page: Int)

    suspend fun getByID(id:Int): Image?

    suspend fun clear()

    fun getListFlow(): Flow<List<Image>>


}