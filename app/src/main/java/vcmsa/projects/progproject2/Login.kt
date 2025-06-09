package vcmsa.projects.progproject2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var dbHelper: DBHelper

    //Intent usage for activity navigation:
    //Official documentation on launching activities:
    //https://developer.android.com/guide/components/intents-filters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//ViewCompat and WindowInsetsCompat for system bar handling:
//Based on Android documentation for managing window insets:
//https://developer.android.com/reference/androidx/core/view/ViewCompat
//https://developer.android.com/reference/androidx/core/view/WindowInsetsCompat

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //DBHelper class and SQLite authentication logic:
        //Modeled after Android SQLite database usage patterns:
        //https://developer.android.com/training/data-storage/sqlite

        dbHelper = DBHelper(this)

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_btn)  // renamed from register_btn to login_btn

        //Login button logic, EditText field validation, and Toasts:
        //Inspired by Android developer best practices:
        //https://developer.android.com/guide/topics/ui/controls/button
        //https://developer.android.com/guide/topics/ui/notifiers/toasts
        //https://developer.android.com/reference/android/widget/EditText

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else if (username == "admin" && password == "admin") {
                // Simulate success and navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}