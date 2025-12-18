package com.example.bchatbox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bchatbox.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity() {

    private lateinit var userDataTextView: TextView
    private lateinit var userEmailEditText: EditText
    private lateinit var newUsernameEditText: EditText
    private lateinit var deleteUserButton: Button
    private lateinit var modifyUserButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        userDataTextView = findViewById(R.id.userDataTextView)
        userEmailEditText = findViewById(R.id.userEmailEditText)
        newUsernameEditText = findViewById(R.id.newUsernameEditText)
        deleteUserButton = findViewById(R.id.deleteUserButton)
        modifyUserButton = findViewById(R.id.modifyUserButton)

        mAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("user")

        fetchUserData()

        deleteUserButton.setOnClickListener {
            val emailToDelete = userEmailEditText.text.toString()
            deleteUser (emailToDelete)
        }

        modifyUserButton.setOnClickListener {
            // Toggle visibility of the new username EditText
            if (newUsernameEditText.visibility == View.GONE) {
                newUsernameEditText.visibility = View.VISIBLE
            } else {
                val emailToModify = userEmailEditText.text.toString()
                val newUsername = newUsernameEditText.text.toString() // Get the new username from the EditText
                modifyUser (emailToModify, newUsername)
                newUsernameEditText.visibility = View.GONE // Hide the EditText after modification
            }
        }
    }

    private fun fetchUserData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = StringBuilder()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    Log.d("User ", "Fetched email: ${user?.email}") // Debug log
                    userData.append("Email: ${user?.email}\n")
                    userData.append("Username: ${user?.name}\n")
                }
                userDataTextView.text = userData.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database Error", error.message)
                // Handle possible errors.
            }
        })
    }

    private fun deleteUser (email: String) {
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    userSnapshot.ref.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Delete User", "User  deleted successfully")
                        } else {
                            Log.e("Delete User", "Failed to delete user")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database Error", error.message)
            }
        })
    }

    private fun modifyUser (email: String, newUsername: String) {
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    userSnapshot.ref.child("name").setValue(newUsername).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Modify User", "User  modified successfully")
                        } else {
                            Log.e("Modify User", "Failed to modify user")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database Error", error.message)
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            //logic for logout
            mAuth.signOut()
            val intent = Intent(this@AdminActivity, LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}