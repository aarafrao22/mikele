package com.example.mikelesimonieventfindercs414hw4

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), ShakeListener {

    // declare components
    private lateinit var searchButton: Button
    private lateinit var img_save: ImageView
    private lateinit var eventTypeInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var recyclerView: RecyclerView
    lateinit var eventsAdapter: EventsAdapter
    private var shakeSensor: ShakeSensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        TODO("Integrate Add to Fav Feature as well")

        // set components
        searchButton = findViewById(R.id.searchbutton)
        eventTypeInput = findViewById(R.id.keywordInput)
        locationInput = findViewById(R.id.cityInput)
        img_save = findViewById(R.id.img_save)
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        eventsAdapter = EventsAdapter(emptyList())
        recyclerView.adapter = eventsAdapter

        shakeSensor = ShakeSensor(this, this)
        shakeSensor!!.registerShakeSensor()

        img_save.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavouritesActivity::class.java))
        }
        // onclick for the search button to search
        searchButton.setOnClickListener {
            it.hideKeyboard() // Call hideKeyboard when the button is clicked
            performSearch(eventTypeInput.text.toString(), locationInput.text.toString())
        }
    }

    // function to hide keyboard provided by professor
    private fun View.hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        shakeSensor?.unregisterShakeSensor()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onShakeDetected() {
        // Show a toast message indicating that the list is cleared
        Toast.makeText(this@MainActivity, "List Cleared!", Toast.LENGTH_SHORT).show()

        // Set the text of eventTypeInput to an empty string
        eventTypeInput.setText("")
        locationInput.setText("")

        // Check if the adapter has items and clear the list if it does
        if (eventsAdapter.itemCount > 0) {
            eventsAdapter.updateData(emptyList())
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }


    // search operation based on user input and errors when trying to search without both location and event type
    private fun performSearch(eventType: String, location: String) {
        when {
            eventType.isBlank() && location.isBlank() -> {
                showAlertDialog(
                    "Search term and Location missing",
                    "Please enter both a search term and location."
                )
            }

            eventType.isBlank() -> {
                showAlertDialog(
                    "Search term missing",
                    "Search term cannot be empty, please enter a search term."
                )
            }

            location.isBlank() -> {
                showAlertDialog("Location missing", "City cannot be empty. Please enter a city.")
            }

            else -> {

                service.searchEvents(API_KEY, eventType, location)
                    .enqueue(object : Callback<EventData> {
                        override fun onResponse(
                            call: Call<EventData>,
                            response: Response<EventData>
                        ) {
                            if (response.isSuccessful && !response.body()?._embedded?.events.isNullOrEmpty()) {
                                response.body()?._embedded?.events?.let { events ->
                                    runOnUiThread { eventsAdapter.updateData(events) }
                                }
                            } else {
                                runOnUiThread {
                                    showAlertDialog(
                                        "No Results Found",
                                        "No events found matching your search. Try again."
                                    )
                                }
                            }
                        }

                        override fun onFailure(call: Call<EventData>, t: Throwable) {
                            runOnUiThread {
                                showAlertDialog(
                                    "Error",
                                    "An error occurred while attempting to retrieve data."
                                )
                            }
                        }
                    })
            }
        }
    }

    // show alert dialog
    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }


    // retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://app.ticketmaster.com/") // Base URL for Ticketmaster API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // service instance
    private val service: TicketMasterService = retrofit.create(TicketMasterService::class.java)

    companion object {
        const val API_KEY = "ZkfhItqO6KxjoCGC2iiVmFIgrogAoxhe" // Use your actual API key
    }
}
