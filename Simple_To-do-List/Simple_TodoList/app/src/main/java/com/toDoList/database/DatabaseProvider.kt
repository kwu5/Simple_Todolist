package com.toDoList.database

import android.content.Context
import androidx.room.Room


object DatabaseProvider {

    @Volatile
    private var instance: AppDatebase? = null

    fun getInstance(context: Context) : AppDatebase = instance ?: synchronized(this){
        instance ?: buildDatabase(context).also { instance = it}
    }

    private fun buildDatabase(context: Context)= Room
        .databaseBuilder(context,AppDatebase ::class.java, "task-database")
        .build()

    }


