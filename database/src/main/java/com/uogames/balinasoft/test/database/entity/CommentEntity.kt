package com.uogames.balinasoft.test.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "comment_table",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("image_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CommentEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("image_id")
    val imageID: Int,
    @ColumnInfo("date")
    val date: Long,
    @ColumnInfo("text")
    val text: String,
    @ColumnInfo("page", defaultValue = "0")
    val page: Int = 0,
)