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

class AmadeusApi {
    private var accessToken=""
    private val tokenExpiresIn=0
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
    //client.close()

    suspend fun getAccessToken(): String {
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
        }
    }

    suspend fun searchHotelByCity(city: String, amenities: List<String>, rating: String) {
        try {
            accessToken=getAccessToken()
            val response: HotelSearchResponse =
                client.get("https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-city?cityCode=$city&radius=50&radiusUnit=KM&amenities=$amenities&ratings=$rating") {
                    headers {
                        bearerAuth(accessToken)
                    }
                }.body()
            print(response.data.size)
        } catch (e: ClientRequestException) {
            println(e.message)
        }
        print("+++++++++++++++++++")
    }
}