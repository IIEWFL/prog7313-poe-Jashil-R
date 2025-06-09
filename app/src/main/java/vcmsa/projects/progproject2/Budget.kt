package vcmsa.projects.progproject2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class BudgetActivity : AppCompatActivity() {

//General App Structure based on Kotlin Android App Development Guide:
//https://developer.android.com/kotlin

    private lateinit var dateInput: EditText
    private lateinit var timeInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var imageButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var cancelButton: Button
    private lateinit var addButton: Button

    private val PICK_IMAGE_REQUEST = 1001
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        // Initialize views
        dateInput = findViewById(R.id.dateInput)
        timeInput = findViewById(R.id.timeInput)
        amountInput = findViewById(R.id.amountInput)
        categoryInput = findViewById(R.id.categoryInput)
        imageButton = findViewById(R.id.imageButton)
        imagePreview = findViewById(R.id.imagePreview)
        cancelButton = findViewById(R.id.cancelButton)
        addButton = findViewById(R.id.addBudgetBtn)

//Date Picker and Time Picker logic based on Android Developer Documentation:
//https://developer.android.com/reference/android/app/DatePickerDialog
//https://developer.android.com/reference/android/app/TimePickerDialog

        // Date picker
        dateInput.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                dateInput.setText("$year-${month + 1}-$dayOfMonth")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time picker
        timeInput.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                timeInput.setText(String.format("%02d:%02d", hour, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

//Image Selection and Preview functionality adapted from:
//https://developer.android.com/guide/topics/providers/content-providers
//https://developer.android.com/training/camera/photobasics

        // Image picker
        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Add button action
        addButton.setOnClickListener {
            val date = dateInput.text.toString()
            val time = timeInput.text.toString()
            val amount = amountInput.text.toString()
            val category = categoryInput.text.toString()

//Database Helper and Insert Logic based on Android SQLite Database Tutorial:
//https://developer.android.com/training/data-storage/sqlite

            val dbHelper = DBHelper(this)
            val isInserted = dbHelper.insertBudgetData(date, time, amount, category)

            if (isInserted) {
                Toast.makeText(this, "Budget saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save budget", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancel button action
        cancelButton.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            imagePreview.setImageURI(selectedImageUri)
            imagePreview.visibility = ImageView.VISIBLE
        }
    }
}