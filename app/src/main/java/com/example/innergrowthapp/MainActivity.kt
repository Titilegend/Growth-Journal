package com.example.innergrowthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.innergrowthapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.database.ValueEventListener



class MainActivity : AppCompatActivity() {
    lateinit var  mainBinding : ActivityMainBinding
    val database:FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("entries")
    val dataList = ArrayList<JournalEntries>()
    lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        mainBinding.floatingActionButton2.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }
        mainBinding.logoutbutton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        retrieveDataFromDataBase()

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //to get id and position of the user to be deleted
                val id =  adapter.getJournalId(viewHolder.adapterPosition)

                myReference.child(id).removeValue()
                //delete()

                Toast.makeText(applicationContext,"The entry was deleted", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(mainBinding.recyclerView)
    }


       fun retrieveDataFromDataBase(){
            myReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataList.clear()
                    for(entries in snapshot.children){
                        val journalEntry = entries.getValue(JournalEntries::class.java)
                        if(journalEntry!=null){
                            println("date:${journalEntry.journalId}")
                            println("title:${journalEntry.journalId}")
                            println("description:${journalEntry.journalId}")
                            println("----------------------------")

                            dataList.add(journalEntry)
                        }

                        adapter = Adapter(dataList)
                        //layout design
                        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                        mainBinding.recyclerView.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


}