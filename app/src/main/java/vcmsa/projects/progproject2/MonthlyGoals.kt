package vcmsa.projects.progproject2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MonthlyGoals : AppCompatActivity() {

    private lateinit var minGoalInput: EditText
    private lateinit var maxGoalInput: EditText
    private lateinit var monthInput: EditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DBHelper

    //UI and Activity Lifecycle:
    //Based on standard Android activity lifecycle and view binding:
    //https://developer.android.com/guide/components/activities/activity-lifecycle
    //https://developer.android.com/reference/android/app/Activity#setContentView(int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_goals)

        //WindowInsetsCompat and ViewCompat for padding UI under system bars:
        //AndroidX core library reference:
        //https://developer.android.com/reference/androidx/core/view/WindowInsetsCompat
        //https://developer.android.com/reference/androidx/core/view/ViewCompat

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        monthInput = findViewById(R.id.goalMonthEditText)
        minGoalInput = findViewById(R.id.minGoalInput)
        maxGoalInput = findViewById(R.id.maxGoalInput)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveGoalBtn)

        dbHelper = DBHelper(this)

        cancelButton.setOnClickListener {
            finish()
        }

        //Button listeners and data validation:
        //Follows Android best practices for EditText input validation and click listeners:
        //https://developer.android.com/guide/topics/ui/controls/button
        //https://developer.android.com/reference/android/widget/EditText

        saveButton.setOnClickListener {
            val month = monthInput.text.toString().trim()
            val minGoal = minGoalInput.text.toString().trim()
            val maxGoal = maxGoalInput.text.toString().trim()

            if (month.isNotEmpty() && minGoal.isNotEmpty() && maxGoal.isNotEmpty()) {
                val goalDescription = "Min: $minGoal, Max: $maxGoal"
                val isInserted = dbHelper.insertMonthlyGoal(month, goalDescription)

                if (isInserted) {
                    Toast.makeText(this, "Goal saved", Toast.LENGTH_SHORT).show()
                    monthInput.text.clear()
                    minGoalInput.text.clear()
                    maxGoalInput.text.clear()
                } else {
                    Toast.makeText(this, "Failed to save goal", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
}