package com.amadeus.hotel

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

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

    suspend fun searchHotelOffers(hotelIds: String,checkInDate: String,checkOutDate:String,adults:String): Pair<List<HotelOffers>, String>  {
        return amadeusApi.searchHotelOffers(hotelIds=hotelIds,checkInDate=checkInDate,checkOutDate=checkOutDate,adults=adults)
    }

    suspend fun bookHotelRooms(offerId:String): Pair<List<BookingData>, String>{
        // Provide guest details from the client
         val guest= buildJsonObject {
                putJsonObject("name"){
                    put("title", "Mr")
                    put("firstName", "Bob")
                    put("lastName", "Smith")
                }
                putJsonObject("contact"){
                    put("phone", "+33679271474")
                    put("email", "bob.smith@email.com")
                }
            }
        // Provide payment information from the client
        val payment= buildJsonObject {
                put("method", "creditCard")
            putJsonObject("card") {
                put("vendorCode", "VI")
                put("cardNumber", "0000000000000000")
                put("expiryDate", "2026-01")
            }
        }
        val json = buildJsonObject {
            putJsonObject("data") {
                put("offerId", offerId)
                putJsonArray("guests") {
                    add(guest)
                }
                putJsonArray("payments") {
                    add(payment)
                }
            }
        }
        return  amadeusApi.bookHotelOffer(json=json)
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