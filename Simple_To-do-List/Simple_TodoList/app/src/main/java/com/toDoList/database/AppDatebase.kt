package com.toDoList.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class AppDatebase : RoomDatabase() {

    abstract fun taskDao() : TaskDAO
}