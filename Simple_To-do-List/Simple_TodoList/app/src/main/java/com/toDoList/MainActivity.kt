package com.toDoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.toDoList.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.mainLinearLayoutInbox.setOnClickListener {
//            Log.d("Main:","trigger")

//            startProfileActivity()
            val intent = Intent(this, InboxActivity::class.java)
            startActivity(intent)
        }

        binding.mainLinearLayoutToday.setOnClickListener {
            val intent = Intent(this, TodayActivity::class.java)
            startActivity(intent)
        }


        binding.mainLinearLayoutUpcomingWeek.setOnClickListener {
            val intent = Intent(this,UpComingActivity::class.java)
            startActivity(intent)
        }
    }


}