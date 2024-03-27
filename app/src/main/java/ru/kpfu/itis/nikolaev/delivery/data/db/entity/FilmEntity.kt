package ru.kpfu.itis.nikolaev.delivery.data.db.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "films",
)
data class FilmEntity (
    @PrimaryKey val id : String,
    @ColumnInfo(name = "title")
    val title : String,
)
