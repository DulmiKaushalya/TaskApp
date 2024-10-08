package com.example.taskapp

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)
        // Populate the Spinner with predefined categories
        val categories = arrayOf("Work", "Personal", "Shopping", "Other")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val selectedCategory = binding.categorySpinner.selectedItem.toString() // Get the selected category

            // Show confirmation dialog
            showConfirmationDialog(title, content, selectedCategory)
        }
    }

    private fun showConfirmationDialog(title: String, content: String, category: String) {
        // Create an AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Save")
        builder.setMessage("Do you want to save this note?")
        builder.setPositiveButton("Yes") { dialog, which ->
            val note = Note(0, title, content, category)
            db.insertNote(note)
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss() // Just dismiss the dialog
        }

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}
