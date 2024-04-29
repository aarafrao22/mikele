package com.example.mikelesimonieventfindercs414hw4

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavouritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_favourites)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        eventsAdapter = EventsAdapter(emptyList())
        recyclerView.adapter = eventsAdapter



        fetchDataFromFirebase()


    }

    private fun fetchDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().reference.child("events")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    val eventsList = mutableListOf<Event>()

                    eventsAdapter.updateData(eventsList)

//                    for (eventSnapshot in dataSnapshot.children) {
//
//                        // Convert each child node to an Event object
//                        val event = eventSnapshot.getValue(Event::class.java)
//                        event?.let { eventsList.add(it) }
//
//
//                    }
                    // Now, eventsList contains all the events fetched from the database
                    // You can use this list as needed, such as displaying it in a RecyclerView
                } else {
                    Log.d("MainActivity", "No data found in the database")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors that occurred while fetching data
                Log.e("MainActivity", "Failed to read value.", databaseError.toException())
            }
        })
    }

    private fun fetchData() {


    }
}