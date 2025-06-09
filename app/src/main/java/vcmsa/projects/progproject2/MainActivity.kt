package vcmsa.projects.progproject2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.R
import vcmsa.projects.myapplication.module.BudgetEntry


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup buttons
        findViewById<Button>(R.id.addBudgetBtn).setOnClickListener {
            startActivity(Intent(this, BudgetActivity::class.java))
        }

        findViewById<Button>(R.id.addExpenseBtn).setOnClickListener {
            startActivity(Intent(this, ExpenseEntryActivity::class.java))
        }

        findViewById<Button>(R.id.monthlyGoalsBtn).setOnClickListener {
            startActivity(Intent(this, MonthlyGoals::class.java))
        }

        findViewById<Button>(R.id.viewBudgetBtn).setOnClickListener {
            startActivity(Intent(this, ViewBudgets::class.java))
        }

        findViewById<Button>(R.id.viewListBtn).setOnClickListener {
            startActivity(Intent(this, ViewList::class.java))
        }

        findViewById<Button>(R.id.viewGoalsBtn).setOnClickListener {
            startActivity(Intent(this, ListGoals::class.java))
        }

        // Setup RecyclerView for Firebase data
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadFromFirebase() // Call the Firebase data load
    }

    private fun loadFromFirebase() {
        val ref = FirebaseDatabase.getInstance().getReference("entries")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BudgetEntry>()
                for (entrySnap in snapshot.children) {
                    val entry = entrySnap.getValue(BudgetEntry::class.java)
                    if (entry != null) list.add(entry)
                }
                recyclerView.adapter = EntryAdapter(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
