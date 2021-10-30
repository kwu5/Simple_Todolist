package com.toDoList

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.toDoList.database.Task
import com.toDoList.databinding.ActivityProfileBinding
import com.toDoList.repository.TaskRepository
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val repository by lazy { TaskRepository(applicationContext) }


    var textview_date: TextView? = null
    var textview_time: TextView? = null
    var cal = Calendar.getInstance()
    var currentCal = Calendar.getInstance()

    companion object{
        private const val EXTRAS_TASK = "EXTRAS_TASK"
        private const val SAVE = "SAVE"
        private const val LAUNCH_PROFILEACTIVITY : Int = 0



        fun start(activity: Activity, task: Task) {
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.putExtra(EXTRAS_TASK, task)
            intent.putExtra(SAVE, "save")
            activity.startActivityForResult(intent, LAUNCH_PROFILEACTIVITY)
        }

        fun get():Int = LAUNCH_PROFILEACTIVITY
    }




    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // get the references from layout file
        textview_date = binding.TextDateChange
        textview_time = binding.TextTimeChange
        textview_date!!.text = "--/--/----"
        textview_time!!.text = "--:--"


        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {


            //null check
            if(binding.TitleInput.editableText.isEmpty()){
                binding.LayoutTextTitle.error = "Title must not be empty"
            }else if (binding.TextDateChange.text.equals("--/--/----")){
                binding.TextDateChange.error = "Date must not be empty"
            }else if (binding.TextTimeChange.text.equals("--:--")){
                binding.TextTimeChange.error = "Time must not be empty"
            }else {

                // save task
                if (intent.hasExtra(SAVE)){
                    var task = intent.extras?.getParcelable<Task>(EXTRAS_TASK)
                    updateTask(task)
                    Log.e("profile:","saveButton_update")
                } else {
                    //create task
                    createTask()
                }
                var intent = Intent()
                setResult(Activity.RESULT_OK,intent)
                finish()
                }
            }


        // create an OnDateSetListener
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        textview_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                var dialog = DatePickerDialog(this@ProfileActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH))
                    dialog.datePicker.minDate = cal.timeInMillis
                    dialog.show()
            }
        })

        //time picker
        textview_time!!.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                //check if past time selected
                if(cal.timeInMillis  < currentCal.timeInMillis ){
                    Toast.makeText(applicationContext,"Invalid time",Toast.LENGTH_LONG).show()
//                    textview_time!!.text = SimpleDateFormat("HH:mm").format(currentCal.time)
                }else{
                    textview_time!!.text = SimpleDateFormat("HH:mm").format(cal.time)
                }



            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()


        }

        //show data
        intent.extras?.getParcelable<Task>(EXTRAS_TASK)?.let { task ->
            showData(task)
        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTask() {


        val title = binding.TitleInput.text?.toString().toString()
        val description = binding.DescriptionInput.text.toString()
        val date = binding.TextDateChange.text?.toString().toString()
        val time = binding.TextTimeChange.text?.toString().toString()


        Log.e("Profile:","createTask")
        Log.e("Profile","$date,   $time")

        val task = Task(title, description, date, time, getSction(date))
        repository.createNewTask(task)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSction(date: String): String {

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val dt = LocalDate.parse(date, formatter)

//        Log.e("Profile", "getSection: ${dt.dayOfMonth}  ${dt.month}")


//        Log.e("1111111111111111111111", "${dt.monthValue}      $month")
//        Log.e("2222222222222222222222", "${dt.dayOfMonth}      $day")

//    var currentMonth = cal.get(Calendar.MONTH)+1
//    var currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        if (dt.monthValue == currentCal.get(Calendar.MONTH) + 1 && (dt.dayOfMonth == currentCal.get(
                Calendar.DAY_OF_MONTH
            ))
        ) {
            return "TODAY"
        } else {
            return "UPCOMING"

        }
    }


    private fun updateTask(task: Task?) {
        val ntitle = binding.TitleInput.text
        val ndescription = binding.DescriptionInput.text
        val ndate = binding.TextDateChange.text
        val ntime = binding.TextTimeChange.text

        Log.e("Profile:","updateTask")

        task?.title = ntitle.toString()
        task?.description = ndescription.toString()
        task?.date = ndate.toString()
        task?.time = ntime.toString()

        val nsection = getSction(ndate.toString())
        task?.section = nsection
        repository.updateTask(task!!)
    }

    private fun showData(task: Task) {

        binding.TitleInput.setText(task.title)
        binding.DescriptionInput.setText(task.description)
        binding.TextDateChange.text = task.date
        binding.TextTimeChange.text = task.time

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }




}





