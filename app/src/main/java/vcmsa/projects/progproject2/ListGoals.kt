package vcmsa.projects.progproject2

import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.progproject2.R.id.listGoalsView

class ListGoals : AppCompatActivity() {

    //ListView and ArrayAdapter Usage:
    //Based on standard practices from the official Android developer guide:
    //https://developer.android.com/reference/android/widget/ListView
    //https://developer.android.com/reference/android/widget/ArrayAdapter

    private lateinit var listView: ListView
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_goals)

        listView = findViewById(listGoalsView) // Make sure this ID exists in your XML
        dbHelper = DBHelper(this)

        displayGoals()
    }

    //SQLite Database Querying with Cursor:
    //Inspired by examples from Android developer training:
    //https://developer.android.com/training/data-storage/sqlite
    //getColumnIndexOrThrow reference:
    //https://developer.android.com/reference/android/database/Cursor#getColumnIndexOrThrow(java.lang.String)

    //Cursor Iteration and Safety:
    //Based on common practices for reading from cursors safely:
    //Stack Overflow: https://stackoverflow.com/questions/32914375/android-database-get-column-value

    private fun displayGoals() {
        val cursor: Cursor = dbHelper.getAllMonthlyGoals()
        val goalsList = mutableListOf<String>()

        //Toast Notification for Empty Results:
        // Official Toast usage documentation:
        //https://developer.android.com/guide/topics/ui/notifiers/toasts

        if (cursor.count == 0) {
            Toast.makeText(this, "No goals found", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                val month = cursor.getString(cursor.getColumnIndexOrThrow("month"))
                val goal = cursor.getString(cursor.getColumnIndexOrThrow("goal"))

                goalsList.add("$month: $goal")
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, goalsList)
            listView.adapter = adapter
        }
    }
}