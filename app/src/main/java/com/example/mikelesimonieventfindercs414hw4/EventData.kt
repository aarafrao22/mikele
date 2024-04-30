package com.example.mikelesimonieventfindercs414hw4

data class EventData(
    val _embedded: EmbeddedEvents = EmbeddedEvents()
)

data class EmbeddedEvents(
    val events: List<Event> = listOf()
)

data class Event(
    val name: String = "",
    val images: List<Image> = listOf(),
    val dates: EventDates = EventDates(),
    val _embedded: EventVenue = EventVenue(),
    val url: String = "",
    val priceRanges: List<PriceRange>? = null
) {
    // Empty constructor required by Firebase
    constructor() : this("", listOf(), EventDates(), EventVenue(), "", null)
}

data class PriceRange(
    val type: String = "",
    val min: Double = 0.0,
    val max: Double = 100000.0,
    val currency: String = "USD"
)

data class Image(
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
) {
    // Empty constructor required by Firebase
    constructor() : this("", 0, 0)
}

data class EventDates(
    val start: EventStart = EventStart()
)

data class EventStart(
    val localDate: String = "",
    val localTime: String? = null
)

data class EventVenue(
    val venues: List<Venue> = listOf()
) {
    // Empty constructor required by Firebase
    constructor() : this(listOf())
}

data class Venue(
    val name: String = "",
    val city: City = City(),
    val state: State? = null,
    val address: VenueAddress = VenueAddress()
) {
    // Empty constructor required by Firebase
    constructor() : this("", City(), null, VenueAddress())
}

data class VenueAddress(
    val line1: String = ""
)

data class City(
    val name: String = ""
)

data class State(
    val name: String = ""
)
