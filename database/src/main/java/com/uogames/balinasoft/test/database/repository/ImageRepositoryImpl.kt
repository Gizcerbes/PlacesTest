package com.uogames.balinasoft.test.database.repository

import com.uogames.balinasoft.core.database.ImageRepository
import com.uogames.balinasoft.core.model.image.Image
import com.uogames.balinasoft.test.database.dao.ImageDAO
import com.uogames.balinasoft.test.database.map.ImageMapper.toDTO
import com.uogames.balinasoft.test.database.map.ImageMapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImageRepositoryImpl(
    private val dao: ImageDAO
): ImageRepository {


    override suspend fun saveImages(vararg image: Image) {
        dao.save(*image.map { it.toEntity() }.toTypedArray())
    }

    override suspend fun delete(image: Image) {
       dao.delete(image.toEntity())
    }

    override suspend fun deleteByID(id: Int) {
        dao.deleteByID(id)
    }

    override suspend fun deleteInstead(ids: List<Int>, page: Int) {
        dao.deleteInsteadOnPage(ids, page)
    }

    override suspend fun deleteOnPage(page: Int) {
        dao.deleteOnPage(page)
    }

    override suspend fun getByID(id: Int): Image? {
        return dao.getByID(id)?.toDTO()
    }

    override suspend fun clear() {
        dao.clear()
    }

    override fun getListFlow(): Flow<List<Image>> {
      return dao.getListFlow().map { list -> list.map { it.toDTO() } }
    }


}