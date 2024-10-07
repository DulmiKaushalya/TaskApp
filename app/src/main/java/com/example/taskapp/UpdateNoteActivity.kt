package com.example.taskapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskapp.databinding.ActivityUpdateBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db : NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
               super.onCreate(savedInstanceState)
               binding = ActivityUpdateBinding.inflate(layoutInflater)
               setContentView(binding.root)
        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }
        val note = db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)

        binding.updateSaveButton.setOnClickListener{
             val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updateNote = Note(noteId,newTitle,newContent)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this,"Change Saved",Toast.LENGTH_SHORT).show()
        }
    }
}