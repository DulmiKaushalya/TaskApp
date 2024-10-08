package com.example.taskapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notesList: List<Note>, private val context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)
    private var fullNotesList = ArrayList(notesList) // Save the original list for filtering

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notesList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.categoryTextView.text = note.category

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            // Show confirmation dialog before deletion
            showDeleteConfirmationDialog(note)
        }
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        // Create an AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Do you really want to delete this note?")
        builder.setPositiveButton("Yes") { dialog, which ->
            db.deleteNote(note.id)
            refreshData(db.getAllNotes()) // Refresh the list after deletion
            Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss() // Just dismiss the dialog
        }

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun refreshData(newNotes: List<Note>) {
        notesList = newNotes
        fullNotesList = ArrayList(newNotes) // Update the full list whenever data changes
        notifyDataSetChanged()
    }

    fun filter(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            fullNotesList // If no query, show all notes
        } else {
            fullNotesList.filter {
                it.title.contains(query, ignoreCase = true) || // Filter by title
                        it.category.contains(query, ignoreCase = true) // Filter by category
            }
        }
        notesList = filteredList
        notifyDataSetChanged() // Notify the adapter that data has changed
    }
}
