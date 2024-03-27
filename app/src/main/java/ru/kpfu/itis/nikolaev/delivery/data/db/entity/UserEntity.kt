package ru.kpfu.itis.nikolaev.delivery.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
    indices = [Index("email")]
)
data class UserEntity (
    @PrimaryKey val id : String,
    val name : String,
    @ColumnInfo(name = "second_name")
    val secondName : String,
    val patronymic : String?,
    @ColumnInfo(name = "email")
    val emailAddress : String?,
)