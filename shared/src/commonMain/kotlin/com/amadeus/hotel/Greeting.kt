package com.amadeus.hotel

import kotlinx.coroutines.runBlocking

class Greeting {
    private val platform: Platform = getPlatform()
    private val amadeusApi:AmadeusApi= AmadeusApi()

     fun greet(): String {
         runBlocking {
             amadeusApi.getAccessToken()
             amadeusApi.searchHotelByCity(city = "PAR", amenities = listOf("SWIMMING_POOL","SPA","RESTAURANT","WIFI","ROOM_SERVICE"), rating = "3")
         }
        return "Hello, ${platform.name}!"
    }
}