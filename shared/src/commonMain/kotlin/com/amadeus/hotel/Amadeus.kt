package com.amadeus.hotel

class Amadeus {
    private val amadeusApi:AmadeusApi= AmadeusApi()
    private val cityCodes:Map<String,String> = mapOf(
        ("New York" to "NYC"),
        ("Rome" to "ROM"),
        ("Chicago" to "CHI"),
        ("Paris" to "PAR"),
        ("Mayport" to "NRB"),
        ("London" to "LON"),
        ("Tokyo" to "TYO"),
        ("Lagos" to "LOS"),
        ("Delhi" to "DEL")
    )

    private val hotelAmenities:List<String> = listOf(
        "SWIMMING_POOL","SPA","FITNESS_CENTER","RESTAURANT","PARKING","WIFI","ROOM_SERVICE"
    )

    private val hotelRatings:List<Int> = listOf(1,2,3,4,5)

    suspend fun searchHotel(city: String, amenities: List<String>, rating: List<Int>): Pair<List<Hotel>, String>  {
        return amadeusApi.searchHotelByCity(city = cityCodes[city]!!, amenities = amenities.joinToString(separator = ","), rating = rating.joinToString(separator = ","))
    }

    suspend fun searchHotelOffers(hotelIds: String,checkInDate: String,checkOutDate:String): Pair<List<HotelOffers>, String>  {
        return amadeusApi.searchHotelOffers(hotelIds=hotelIds,checkInDate=checkInDate,checkOutDate=checkOutDate)
    }
    fun getCityCodes():Map<String,String>{
        return  cityCodes
    }

    fun getHotelAmenities():List<String>{
        return  hotelAmenities
    }

    fun getHotelRatings():List<Int>{
        return  hotelRatings
    }
}