package com.toDoList.database

import androidx.room.*


@Dao
interface TaskDAO {

    @Query("SELECT * FROM task")
    fun getAll():List<Task>

    @Insert
    fun insertAll(taskList: List<Task>)

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task:Task)

    @Delete
    fun delete(task: Task)


    @Query("SELECT * FROM task WHERE section LIKE :Section")
    fun getSection(Section:String): List<Task>





}