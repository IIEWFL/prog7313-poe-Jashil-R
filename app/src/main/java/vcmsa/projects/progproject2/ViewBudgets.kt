package vcmsa.projects.progproject2

import android.database.Cursor
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ViewBudgets : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var dbHelper: DBHelper
    private lateinit var startDateInput: EditText
    private lateinit var endDateInput: EditText
    private lateinit var filterButton: Button

    //UI Elements and Event Handling:
    //Usage of EditText, Button, and ListView with adapters:
    //https://developer.android.com/reference/android/widget/EditText
    //https://developer.android.com/reference/android/widget/Button
    //https://developer.android.com/reference/android/widget/ListView
    //https://developer.android.com/reference/android/widget/ArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_budgets)

        listView = findViewById(R.id.budgetListView)
        dbHelper = DBHelper(this)

        startDateInput = findViewById(R.id.startDateInput)
        endDateInput = findViewById(R.id.endDateInput)
        filterButton = findViewById(R.id.filterButton)

        displayBudgets()

        //Error Handling and Toast Notifications:
        //Based on Toast utility to notify users of UI actions or data status:
        //https://developer.android.com/guide/topics/ui/notifiers/toasts

        filterButton.setOnClickListener {
            val start = startDateInput.text.toString()
            val end = endDateInput.text.toString()

            if (start.isNotEmpty() && end.isNotEmpty()) {
                displayFilteredBudgets(start, end)
            } else {
                Toast.makeText(this, "Please enter both dates", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayBudgets() {
        val cursor: Cursor = dbHelper.getAllBudgets()
        val budgetList = mutableListOf<String>()

        if (cursor.count == 0) {
            Toast.makeText(this, "No budgets found", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                val amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))

                budgetList.add("[$date $time] $category: R$amount")
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, budgetList)
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
}
