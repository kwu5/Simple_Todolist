package com.toDoList.repository

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.toDoList.database.DatabaseProvider
import com.toDoList.database.Task
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TaskRepository ( context: Context) {

    private val database = DatabaseProvider.getInstance(context.applicationContext)
    private val excutor = Executors.newSingleThreadExecutor()





    fun loadAllDataFromDatabase(callback: (List<Task>) -> Unit){
        excutor.execute {
            callback(database.taskDao().getAll())
        }
    }

    fun loadTodayDataFromDatabase(callback: (List<Task>) -> Unit){
        excutor.execute {
            callback(database.taskDao().getSection("TODAY"))
        }
    }

    fun loadUpcomingDataFromDatabase(callback: (List<Task>) -> Unit){
        excutor.execute {
            callback(database.taskDao().getSection("UPCOMING"))
        }
    }

    fun createNewTask(task: Task){
        excutor.execute {
            database.taskDao().insert(task)
            Log.d(javaClass.name,"insertTask")


        }
    }

    fun updateTask(task: Task){
        excutor.execute {
            database.taskDao().update(task)
            Log.d(javaClass.name,"updateTask")
        }
    }


        fun deleteTask(task: Task){
            excutor.execute {
                database.taskDao().delete(task)
                Log.d(javaClass.name,"deleteTask")
            }
        }


}