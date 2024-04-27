package com.example.mikelesimonieventfindercs414hw4

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(private var events: List<Event>) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    // viewholder to items in recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_layout, parent, false)
        return EventViewHolder(view)
    }

    // binds data to viewholder
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    // returns number of items in data set
    override fun getItemCount(): Int {
        return events.size
    }

    // view references to update items content
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.Image)
        val eventNameTextView: TextView = itemView.findViewById(R.id.EventName)
        val eventDateTimeTextView: TextView = itemView.findViewById(R.id.EventDateTime)
        val eventLocationTextView: TextView = itemView.findViewById(R.id.EventLocation)
        val venueNameTextView: TextView = itemView.findViewById(R.id.VenueName)
        val priceRangeTextView: TextView = itemView.findViewById(R.id.PriceRange)
        val buttonLink: Button = itemView.findViewById(R.id.ButtonLink)

        @SuppressLint("SetTextI18n")
        fun bind(event: Event) {
            eventNameTextView.text = event.name

            // load and display event image
            val highestQualityImage = event.images.maxByOrNull { it.width + it.height }
            Glide.with(itemView.context).load(highestQualityImage?.url).into(imageView)

            // load and display date and time
            val formattedDateTime = formatDateTime(event.dates.start.localDate, event.dates.start.localTime)
            eventDateTimeTextView.text = "Date: $formattedDateTime"

            venueNameTextView.text = event._embedded.venues.joinToString { "${it.name}, ${it.city.name}, ${it.state?.name ?: ""}".trim() }

            // load and display address by order
            val venueAddress = event._embedded.venues.firstOrNull()?.let { venue ->
                "Address: ${venue.address.line1}, ${venue.city.name}, ${venue.state?.name ?: ""}".trim()
            } ?: "Venue details not available"
            eventLocationTextView.text = venueAddress

            priceRangeTextView.text = formatPriceRange(event.priceRanges)

            // open the event URL when button pressed
            buttonLink.setOnClickListener {
                val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                itemView.context.startActivity(urlIntent)
            }
        }

        // formats price range to not show when theres no prices
        private fun formatPriceRange(priceRanges: List<PriceRange>?): String {
            if (priceRanges.isNullOrEmpty()) {
                return ""
            } else {
                val firstRange = priceRanges.first()
                return "Price Range: ${firstRange.min.formatAsCurrency(firstRange.currency)} - ${firstRange.max.formatAsCurrency(firstRange.currency)}"
            }
        }

        private fun Double.formatAsCurrency(currency: String): String {
            return String.format("$%,.2f %s", this, currency)
        }

        // formats date and time from strings in better format
        private fun formatDateTime(localDate: String, localTime: String?): String {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("MM/dd/yyyy 'at' h:mm a", Locale.getDefault())
            val date = inputDateFormat.parse(localDate)
            var formattedDate = date?.let { outputDateFormat.format(it) } ?: "Date not available"
            localTime?.let {
                val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(it)
                val formattedTime = time?.let { t -> SimpleDateFormat("h:mm a", Locale.getDefault()).format(t) } ?: ""
                formattedDate += if (formattedTime.isNotEmpty()) ", $formattedTime" else ""
            }
            return formattedDate
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
