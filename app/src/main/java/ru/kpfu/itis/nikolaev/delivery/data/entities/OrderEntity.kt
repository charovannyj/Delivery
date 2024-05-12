package ru.kpfu.itis.nikolaev.delivery.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "orders",
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "address_from")
    val addressFrom: String,
    @ColumnInfo(name = "address_to")
    val addressTo: String,
    val price: Int,
    val dimensions: String,
    @ColumnInfo(name = "uid_sender")
    val uidSender: String,
    @ColumnInfo(name = "uid_recipient")
    val uidRecipient: String,
    val date: Date
)