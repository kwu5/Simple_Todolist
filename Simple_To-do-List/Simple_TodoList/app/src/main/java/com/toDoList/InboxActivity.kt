package com.toDoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.toDoList.adapter.MainAdapter
import com.toDoList.database.Task
import com.toDoList.databinding.ActivityInboxBinding
import com.toDoList.repository.TaskRepository


class InboxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInboxBinding
    private lateinit var taskList: List<Task>
    private val adapter = MainAdapter { task ->
        ProfileActivity.start(this, task )
    }


    private val repository by lazy { TaskRepository(applicationContext) }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityInboxBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //action bar
        setSupportActionBar(binding.InboxMaterialToolBar)
        binding.InboxMaterialToolBar.setNavigationOnClickListener {
            finish()
        }

        //adapter
        binding.InboxRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.InboxRecyclerView.adapter = adapter
        loadDataFromDatabase()

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.InboxRecyclerView)




        //addButton
        binding.AddInboxButton.setOnClickListener{
            createNewTask()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==  ProfileActivity.get()){
            if(resultCode == RESULT_OK) {
                loadDataFromDatabase()
            }
        }
    }

    private fun createNewTask() {
        Log.i(javaClass.name,"createNewTask")
        val intent = Intent(this,ProfileActivity::class.java)
        this.startActivityForResult(intent,ProfileActivity.get())
    }

    private fun loadDataFromDatabase() {
        repository.loadAllDataFromDatabase { TaskList: List<Task> ->
            runOnUiThread {
                taskList = TaskList
                Log.d(javaClass.name,"loadDataFromDatabase")
                adapter.setData(taskList)
            }
        }

    }


    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var postion = viewHolder.adapterPosition


            when(direction){
                ItemTouchHelper.LEFT -> {
                    val currentTask = taskList[postion]
                    repository.deleteTask(currentTask)
                    loadDataFromDatabase()

                    Snackbar.make(binding.InboxRecyclerView, "${currentTask.title} is deleted",Snackbar.LENGTH_LONG)
                        .setAction("Undo", View.OnClickListener {
                            repository.createNewTask(currentTask)
                            adapter.notifyItemInserted(postion)
                            loadDataFromDatabase()
                        }).show()
                }
            }



        }

    }


}




