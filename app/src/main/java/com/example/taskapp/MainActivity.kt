package com.example.taskapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var allNotes: List<Note>//*****

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
        allNotes = db.getAllNotes()//****
        //notesAdapter = NotesAdapter(db.getAllNotes(), this)
        notesAdapter = NotesAdapter(allNotes, this)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }



        // Search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchNotes(it)
                }
                return true
            }
        })
    }
    private fun searchNotes(query: String) {
        val filteredNotes = allNotes.filter {
            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
        }
        notesAdapter.refreshData(filteredNotes)
    }



    override fun onResume() {
        super.onResume()
        // Refresh allNotes list whenever activity resumes
        allNotes = db.getAllNotes()
        // If there is a search query, apply it again to refresh the filtered list
        val currentQuery = binding.searchView.query.toString()
        if (currentQuery.isNotEmpty()) {
            searchNotes(currentQuery)
        } else {
            // If there's no search query, refresh the RecyclerView with all notes
            notesAdapter.refreshData(allNotes)
        }
    }
}
