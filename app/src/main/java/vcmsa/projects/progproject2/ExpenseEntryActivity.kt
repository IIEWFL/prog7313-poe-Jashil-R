package vcmsa.projects.progproject2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import vcmsa.projects.myapplication.module.ExpenseEntry
import java.util.*

class ExpenseEntryActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_expense)

        // Initialize views
        dateInput = findViewById(R.id.dateInput)
        timeInput = findViewById(R.id.timeInput)
        amountInput = findViewById(R.id.amountInput)
        categoryInput = findViewById(R.id.categoryInput)
        imageButton = findViewById(R.id.imageButton)
        imagePreview = findViewById(R.id.imagePreview)
        cancelButton = findViewById(R.id.cancelButton)
        addButton = findViewById(R.id.addButton)

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
            val imageUriString = selectedImageUri?.toString() ?: ""

            val dbHelper = DBHelper(this)
            val isInserted = dbHelper.insertExpense(date, time, amount, category, imageUriString)

            if (isInserted) {
                Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show()

                // 🔥 Save to Firebase Realtime Database
                val database = FirebaseDatabase.getInstance().getReference("expenses")
                val id = database.push().key ?: return@setOnClickListener
                val expenseEntry = ExpenseEntry(id, date, time, amount, category, imageUriString)

                database.child(id).setValue(expenseEntry)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Saved to Firebase", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Firebase Save Failed", Toast.LENGTH_SHORT).show()
                    }

                finish()
            } else {
                Toast.makeText(this, "Failed to save expense", Toast.LENGTH_SHORT).show()
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
