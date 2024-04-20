package ru.kpfu.itis.nikolaev.delivery.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
)
data class UserEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    val name: String,
    @ColumnInfo(name = "second_name")
    val secondName: String,
    @ColumnInfo(name = "email")
    val emailAddress: String?,
    val password: String?,
)