package com.example.mikelesimonieventfindercs414hw4

import android.location.Address
import java.util.Currency

data class EventData(
    val _embedded: EmbeddedEvents
)

data class EmbeddedEvents(
    val events: List<Event>
)

data class Event(
    val name: String,
    val images: List<Image>,
    val dates: EventDates,
    val _embedded: EventVenue,
    val url: String,
    val priceRanges: List<PriceRange>?
)

data class PriceRange(
    val type: String,
    val min: Double = 0.0,
    val max: Double = 100000.0,
    val currency: String = "USD"
)


data class Image(
    val url: String,
    val width: Int,
    val height: Int
)

data class EventDates(
    val start: EventStart
)

data class EventStart(
    val localDate: String,
    val localTime: String?
)

data class EventVenue(
    val venues: List<Venue>
)

data class Venue(
    val name: String,
    val city: City,
    val state: State?,
    val address: VenueAddress
)

data class VenueAddress(
    val line1: String
)

data class City(
    val name: String
)

data class State(
    val name: String
)
