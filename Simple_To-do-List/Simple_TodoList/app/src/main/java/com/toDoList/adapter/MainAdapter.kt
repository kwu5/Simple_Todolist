package com.toDoList.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toDoList.database.Task
import com.toDoList.databinding.ItemMainBinding
import com.toDoList.repository.TaskRepository


class MainAdapter(private val onItemClickListener: (Task) -> Unit) : ListAdapter<Task, MainViewHolder>(DIFF_CALLBACK) {

    private var originalList: List<Task> = ArrayList()


    fun setData(list: List<Task>){
        originalList = list
        super.submitList(list)
        Log.d("MainAdapter:", "submitted list")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainBinding.inflate(inflater,parent,false)
        return MainViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }




}

private val DIFF_CALLBACK : DiffUtil.ItemCallback<Task> = object: DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }


}

class MainViewHolder(private val binding: ItemMainBinding,private val onItemClickListener: (Task) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(task: Task) {
        binding.TaskConstraintLayout.setOnClickListener{ onItemClickListener(task)}
//        binding.TaskCheckbox.setOnClickListener { deleteTask(task) }
        binding.TextItemSection.text = task.section
        binding.TextItemTitle.text =task.title

        if(task.section.equals("TODAY")){
            binding.TextItemTime.text = task.time
        }
    }


}





