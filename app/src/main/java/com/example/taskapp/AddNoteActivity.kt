package com.example.taskapp

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.databinding.ActivityAddNoteBinding


class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db:NoteDatabaseHelper

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

        binding.saveButton.setOnClickListener{
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val selectedCategory = binding.categorySpinner.selectedItem.toString() // Get the selected category
            val note =Note(0,title, content,selectedCategory)
            db.insertNote(note)
            finish()
            Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show()
        }

    }
}