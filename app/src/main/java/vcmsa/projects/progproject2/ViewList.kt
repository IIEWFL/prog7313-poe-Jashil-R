package vcmsa.projects.progproject2

import android.database.Cursor
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ViewList : AppCompatActivity() {

    //Android Activity Lifecycle and Layout Binding:
    //Initialization follows standard activity lifecycle management:
    //https://developer.android.com/guide/components/activities/intro-activities
    //https://developer.android.com/reference/android/app/Activity#setContentView(int)

    private lateinit var listView: ListView
    private lateinit var dbHelper: DBHelper
    private lateinit var startDateInput: EditText
    private lateinit var endDateInput: EditText
    private lateinit var filterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list)

        listView = findViewById(R.id.expenseListView)
        startDateInput = findViewById(R.id.startDateInput)
        endDateInput = findViewById(R.id.endDateInput)
        filterButton = findViewById(R.id.filterButton)
        dbHelper = DBHelper(this)

        displayExpenses() // Show all by default

        //Widgets and Event Listeners:
        //Use of EditText, Button, and ListView with listeners and adapters:
        //https://developer.android.com/reference/android/widget/EditText
        //https://developer.android.com/reference/android/widget/Button
        //https://developer.android.com/reference/android/widget/ListView
        //https://developer.android.com/reference/android/widget/ArrayAdapter

        filterButton.setOnClickListener {
            val start = startDateInput.text.toString()
            val end = endDateInput.text.toString()

            //Toast Notifications:
            //User feedback via Toast messages adheres to Android’s UI best practices:
            //https://developer.android.com/guide/topics/ui/notifiers/toasts

            if (start.isNotEmpty() && end.isNotEmpty()) {
                displayFilteredBudgets(start, end)
                // Or use: displayFilteredExpenses(start, end) for expenses
            } else {
                Toast.makeText(this, "Please enter both dates", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayExpenses() {
        val cursor: Cursor = dbHelper.getAllExpenses()
        val expenseList = mutableListOf<String>()

        if (cursor.count == 0) {
            Toast.makeText(this, "No expenses found", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                val amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))

                expenseList.add("[$date $time] $category: R$amount")
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expenseList)
            listView.adapter = adapter
        }
    }

    private fun displayFilteredBudgets(startDate: String, endDate: String) {
        val cursor = dbHelper.getBudgetsBetweenDates(startDate, endDate)
        val filteredList = mutableListOf<String>()

        if (cursor.count == 0) {
            Toast.makeText(this, "No records found in that period", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                val amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))

                filteredList.add("[$date $time] $category: R$amount")
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredList)
        listView.adapter = adapter
    }

    // Optional: use this instead to filter expenses
    private fun displayFilteredExpenses(startDate: String, endDate: String) {
        val cursor = dbHelper.getExpensesBetweenDates(startDate, endDate)
        val filteredList = mutableListOf<String>()

        if (cursor.count == 0) {
            Toast.makeText(this, "No records found in that period", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                val amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))

                filteredList.add("[$date $time] $category: R$amount")
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredList)
        listView.adapter = adapter
    }
}
