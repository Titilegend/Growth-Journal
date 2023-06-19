package com.example.innergrowthapp

import android.app.DatePickerDialog
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.widget.Toast
import com.example.innergrowthapp.databinding.ActivityAddEntryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.xml.datatype.DatatypeConstants.MONTHS

class AddEntryActivity : AppCompatActivity() {
    lateinit var addEntryBinding:ActivityAddEntryBinding
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val journalEntriesRef = database.getReference("entries")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEntryBinding = ActivityAddEntryBinding.inflate(layoutInflater)
        val view = addEntryBinding.root
        setContentView(view)
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        addEntryBinding.editTextDate.setOnClickListener {


            val datePicker =DatePickerDialog(this,DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                addEntryBinding.editTextDate.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }
        addEntryBinding.boldButton.setOnClickListener {
            val selectionStart = addEntryBinding.editTextTextMultiLine.selectionStart
            val selectionEnd = addEntryBinding.editTextTextMultiLine.selectionEnd

            val spannableText = addEntryBinding.editTextTextMultiLine.text as Spannable
            val boldSpan = StyleSpan(Typeface.BOLD)
            if (selectionStart != -1 && selectionEnd != -1){
                if(addEntryBinding.boldButton.isSelected){
                    spannableText.removeSpan(boldSpan)
                }
                else{
                    spannableText.setSpan(boldSpan,selectionStart,selectionEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                addEntryBinding.boldButton.isSelected = !addEntryBinding.boldButton.isSelected
            }
        }
        addEntryBinding.italicButton.setOnClickListener {
            val selectionStart = addEntryBinding.editTextTextMultiLine.selectionStart
            val selectionEnd = addEntryBinding.editTextTextMultiLine.selectionEnd

            val spannableText = addEntryBinding.editTextTextMultiLine.text as Spannable
            val italicSpan = StyleSpan(Typeface.ITALIC)
            if (selectionStart != -1 && selectionEnd != -1){
                if(addEntryBinding.italicButton.isSelected){
                    spannableText.removeSpan(italicSpan)
                }
                else{
                    spannableText.setSpan(italicSpan,selectionStart,selectionEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                addEntryBinding.italicButton.isSelected = !addEntryBinding.italicButton.isSelected
            }
        }
        addEntryBinding.submitButton.setOnClickListener {
            //get user input

            val date = addEntryBinding.editTextDate.text.toString()
            val title = addEntryBinding.textInputEditText.text.toString()
            val description = addEntryBinding.editTextTextMultiLine.text.toString()

            val id: String = journalEntriesRef.push().key.toString()

            //create a new journal entry object
            val journalEntries = JournalEntries(id,date,title,description)

            //save journal entry to database
            journalEntriesRef.child(id).setValue(journalEntries).addOnCompleteListener { task->
                if(task.isSuccessful){
                    //journal entry saved
                    Toast.makeText(this,"Journal Entry Saved", Toast.LENGTH_SHORT).show()
                    finish()


                }
                else{
                    //error saving journal
                    Toast.makeText(this,"error saving journal entry",Toast.LENGTH_SHORT).show()
                }
            }


        }
    }
}