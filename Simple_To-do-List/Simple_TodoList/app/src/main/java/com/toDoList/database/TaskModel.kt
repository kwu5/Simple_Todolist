package com.toDoList.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize
import java.util.*


data class TaskData(val data: List<Task>)


@Entity
@Parcelize
data class Task(


    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description:String?,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "time")
    var time:String,

    @ColumnInfo(name ="section")
    var section: String,

    @PrimaryKey val id: Int? = null

    ): Parcelable







