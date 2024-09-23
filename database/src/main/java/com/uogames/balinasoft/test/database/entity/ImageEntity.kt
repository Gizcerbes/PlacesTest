package com.uogames.balinasoft.test.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class ImageEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("date")
    val date: Long,
    @ColumnInfo("lat")
    val lat: Double,
    @ColumnInfo("lng")
    val lng: Double,
    @ColumnInfo("page", defaultValue = "0")
    val page: Int = 0
)