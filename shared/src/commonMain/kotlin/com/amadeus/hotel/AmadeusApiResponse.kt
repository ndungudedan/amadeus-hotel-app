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
data class Hotel(
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
    @SerialName("lastUpdate") var lastUpdate: String? = null,
    @SerialName("contact"   ) var contact   : Contact? = Contact()
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
    @SerialName("data") var data: ArrayList<Hotel> = arrayListOf(),
    @SerialName("errors") var errors: ArrayList<Errors> = arrayListOf()
)

@Serializable
data class Errors(
    @SerialName("code") var code: Int? = null,
    @SerialName("detail") var detail: String? = null,
    @SerialName("status") var status: Int? = null,
    @SerialName("title") var title: String? = null
)
@Serializable
data class HotelOffersResponse (
    @SerialName("data" ) var data : ArrayList<HotelOffers> = arrayListOf()
)

@Serializable
data class HotelOffers (
    @SerialName("type"      ) var type      : String?           = null,
    @SerialName("hotel"     ) var hotel     : Hotel?            = Hotel(),
    @SerialName("available" ) var available : Boolean?          = null,
    @SerialName("offers"    ) var offers    : ArrayList<Offers> = arrayListOf(),
    @SerialName("self"      ) var self      : String?           = null
)
@Serializable
data class Contact (
    @SerialName("phone" ) var phone : String? = null
)
@Serializable
data class TypeEstimated (
    @SerialName("category" ) var category : String? = null,
    @SerialName("beds"     ) var beds     : Int?    = null,
    @SerialName("bedType"  ) var bedType  : String? = null
)
@Serializable
data class Description (
    @SerialName("text" ) var text : String? = null,
    @SerialName("lang" ) var lang : String? = null
)
@Serializable
data class Room (
    @SerialName("type"          ) var type          : String?        = null,
    @SerialName("name"          ) var name          : String?        = null,
    @SerialName("typeEstimated" ) var typeEstimated : TypeEstimated? = TypeEstimated(),
    @SerialName("description"   ) var description   : Description?   = Description()
)
@Serializable
data class Guests (
    @SerialName("adults" ) var adults : Int? = null
)
@Serializable
data class Taxes (
    @SerialName("amount"   ) var amount   : String? = null,
    @SerialName("currency" ) var currency : String? = null
)
@Serializable
data class Average (
    @SerialName("base" ) var base : String? = null
)
@Serializable
data class Changes (
    @SerialName("startDate" ) var startDate : String? = null,
    @SerialName("endDate"   ) var endDate   : String? = null,
    @SerialName("total"     ) var total     : String? = null
)
@Serializable
data class Variations (
    @SerialName("average" ) var average : Average?           = Average(),
    @SerialName("changes" ) var changes : ArrayList<Changes> = arrayListOf()
)
@Serializable
data class Price (
    @SerialName("currency"   ) var currency   : String?          = null,
    @SerialName("base"       ) var base       : String?          = null,
    @SerialName("total"      ) var total      : String?          = null,
    @SerialName("taxes"      ) var taxes      : ArrayList<Taxes> = arrayListOf(),
    @SerialName("variations" ) var variations : Variations?      = Variations()
)
@Serializable
data class Offers (
    @SerialName("id"           ) var id           : String?      = null,
    @SerialName("checkInDate"  ) var checkInDate  : String?      = null,
    @SerialName("checkOutDate" ) var checkOutDate : String?      = null,
    @SerialName("rateCode"     ) var rateCode     : String?      = null,
    @SerialName("description"  ) var description  : Description? = Description(),
    @SerialName("boardType"    ) var boardType    : String?      = null,
    @SerialName("room"         ) var room         : Room?        = Room(),
    @SerialName("guests"       ) var guests       : Guests?      = Guests(),
    @SerialName("price"        ) var price        : Price?       = Price(),
    @SerialName("self"         ) var self         : String?      = null
)
data class BookingResponse (
    @SerialName("code"   ) var code   : Int?    = null,
    @SerialName("title"  ) var title  : String? = null,
    @SerialName("status" ) var status : Int?    = null,
    @SerialName("warnings" ) var warnings : ArrayList<Warnings> = arrayListOf(),
    @SerialName("data"     ) var data     : ArrayList<BookingData>     = arrayListOf(),
    @SerialName("errors") var errors: ArrayList<Errors> = arrayListOf()
)

data class Warnings (
    @SerialName("code"  ) var code  : Int?    = null,
    @SerialName("title" ) var title : String? = null
)

data class AssociatedRecords (
    @SerialName("reference"        ) var reference        : String? = null,
    @SerialName("originSystemCode" ) var originSystemCode : String? = null
)

data class BookingData (
    @SerialName("type"                   ) var type                   : String?                      = null,
    @SerialName("id"                     ) var id                     : String?                      = null,
    @SerialName("providerConfirmationId" ) var providerConfirmationId : String?                      = null,
    @SerialName("associatedRecords"      ) var associatedRecords      : ArrayList<AssociatedRecords> = arrayListOf()
)