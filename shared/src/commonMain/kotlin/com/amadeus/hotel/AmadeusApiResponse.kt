package com.amadeus.hotel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class AmadeusApiResponse

@Serializable
data class AmadeusOAuth2TokenResponse(
    val type: String,
    val username: String,
    @SerialName("application_name")
    val applicationName: String,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    val state: String,
    val scope: String
)

@Serializable
data class Data(
    @SerialName("chainCode") var chainCode: String? = null,
    @SerialName("iataCode") var iataCode: String? = null,
    @SerialName("dupeId") var dupeId: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("hotelId") var hotelId: String? = null,
    @SerialName("geoCode") var geoCode: GeoCode? = GeoCode(),
    @SerialName("address") var address: Address? = Address(),
    @SerialName("amenities") var amenities: ArrayList<String> = arrayListOf(),
    @SerialName("rating") var rating: Int? = null,
    @SerialName("giataId") var giataId: Int? = null,
    @SerialName("lastUpdate") var lastUpdate: String? = null

)

@Serializable
data class Address(
    @SerialName("countryCode") var countryCode: String? = null
)

@Serializable
data class GeoCode(
    @SerialName("latitude") var latitude: Double? = null,
    @SerialName("longitude") var longitude: Double? = null
)

@Serializable
data class HotelSearchResponse(
    @SerialName("data") var data: ArrayList<Data> = arrayListOf(),
    @SerialName("errors") var errors: ArrayList<Errors> = arrayListOf()
)

@Serializable
data class Errors(
    @SerialName("code") var code: Int? = null,
    @SerialName("detail") var detail: String? = null,
    @SerialName("status") var status: Int? = null,
    @SerialName("title") var title: String? = null

)