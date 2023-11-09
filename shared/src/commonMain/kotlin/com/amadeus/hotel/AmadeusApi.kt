package com.amadeus.hotel

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class AmadeusApi {
    private var accessToken = ""
    private val client = HttpClient() {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    private suspend fun getAccessToken(): String {
        val formData = Parameters.build {
            append("grant_type", "client_credentials")
            append("client_id", "vwNbG2xHs9fyc7eUJrWQa3dugsCLxgA7")
            append("client_secret", "5W4fs4NdMKpBhkhw")
        }

        return try {
            val response: AmadeusOAuth2TokenResponse =
                client.post("https://test.api.amadeus.com/v1/security/oauth2/token") {
                    setBody(FormDataContent(formData))
                    contentType(ContentType.Application.FormUrlEncoded)
                    accept(ContentType.Application.Json)
                }.body()
            print(response.accessToken)
            response.accessToken
        } catch (e: ClientRequestException) {
            println(e.message)
            ""
        } catch (e: Exception) {
            println(e.message)
            ""
        }
    }

    suspend fun searchHotelByCity(
        city: String,
        amenities: String,
        rating: String
    ): Pair<List<Hotel>, String> {
        try {
            accessToken = getAccessToken()
            var url =
                "https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-city?cityCode=$city&radius=50&radiusUnit=KM"
            if (amenities.isNotEmpty()) {
                url = "$url&amenities=$amenities"
            }
            if (rating.isNotEmpty()) {
                url = "$url&ratings=$rating"
            }
            val response: HotelSearchResponse =
                client.get(url) {
                    headers {
                        bearerAuth(accessToken)
                    }
                }.body()
            if (response.errors.isNotEmpty()) {
                return Pair(emptyList(), response.errors.first().detail ?: "An error occurred")
            }
            return Pair(response.data, "")
        } catch (e: ClientRequestException) {
            return Pair(emptyList(), e.message ?: "An error occurred")
        } catch (e: Exception) {
            return Pair(emptyList(), e.message ?: "An error occurred")
        }
    }

    suspend fun searchHotelOffers(
        hotelIds: String,
        checkInDate: String,
        checkOutDate:String,
        adults:String
    ): Pair<List<HotelOffers>, String> {
        try {
            accessToken = getAccessToken()
            var url =
                "https://test.api.amadeus.com/v3/shopping/hotel-offers?adults=$adults&includeClosed=false&bestRateOnly=true&checkInDate=$checkInDate&checkOutDate=$checkOutDate"
            if (hotelIds.isNotEmpty()) {
                url = "$url&hotelIds=$hotelIds"
            }
            val response: HotelOffersResponse =
                client.get(url) {
                    headers {
                        bearerAuth(accessToken)
                    }
                }.body()
            return Pair(response.data, "")
        } catch (e: ClientRequestException) {
            return Pair(emptyList(), e.message ?: "An error occurred")
        } catch (e: Exception) {
            return Pair(emptyList(), e.message ?: "An error occurred")
        }
    }

    suspend fun bookHotelOffer(
        json: JsonObject,
    ): Pair<List<BookingData>, String> {
        try {
            accessToken = getAccessToken()
            val url =
                "https://test.api.amadeus.com/v1/booking/hotel-bookings"
            val response: BookingResponse =
                client
                    .post(url) {
                    headers {
                        bearerAuth(accessToken)
                    }
                        contentType(ContentType.Application.Json)
                        setBody(json)
                }.body()
                return Pair(response.data,response.title?:"")
        } catch (e: ClientRequestException) {
            return Pair(emptyList(), e.message ?: "An error occurred")
        } catch (e: Exception) {
            return Pair(emptyList(), e.message ?: "An error occurred")
        }
    }

}