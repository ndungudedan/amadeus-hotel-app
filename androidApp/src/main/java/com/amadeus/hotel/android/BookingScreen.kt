package com.amadeus.hotel.android

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amadeus.hotel.Amadeus
import com.amadeus.hotel.HotelOffers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavHostController, hotelId: String) {
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("No Hotel Rooms") }
    var bookingResponse by remember { mutableStateOf("") }
    var hotelOffers by remember { mutableStateOf<HotelOffers?>(null) }
    var adultCount by remember { mutableStateOf("1") }
    val checkInDateState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val checkOutDateState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Check In")
                DatePickerDemo(state = checkInDateState)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Check Out")
                DatePickerDemo(state = checkOutDateState)
            }
        }
        TextField(
            value = adultCount,
            onValueChange = {
                            adultCount=it
            },
            label = { Text(text = "No.of Guests") },
        )
        Button(onClick = {
            coroutineScope.launch {
                if (checkInDateState.selectedDateMillis != null && checkOutDateState.selectedDateMillis != null) {
                    errorMessage = "Loading"
                    val res = Amadeus().searchHotelOffers(
                        hotelIds = hotelId,
                        checkInDate = dateFormat.format(Date(checkInDateState.selectedDateMillis!!)),
                        checkOutDate = dateFormat.format(Date(checkOutDateState.selectedDateMillis!!)),
                        adults = adultCount
                    )
                    if (res.second.isNotEmpty()) {
                        errorMessage = res.second
                    }
                    hotelOffers = res.first.firstOrNull()
                    errorMessage = "No Hotel Rooms Found"
                }
            }
        }) {
            Text(text = "Search")
        }
        if (hotelOffers == null) {
            Text(text = errorMessage, modifier = Modifier.padding(8.dp))
        } else {
            Text(
                text = "${hotelOffers?.hotel?.name}",
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(hotelOffers!!.offers) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.medium,
                        //elevation = 5.dp,
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                text = "Price: ${it.price?.currency} ${it.price?.total} @${it.price?.variations?.average?.base ?: ""} ${it.price?.taxes?.first()?.pricingFrequency ?: ""}"
                            )
                            Text(
                                text = "Payment Type: ${it.policies?.paymentType}"
                            )
                            if (it.boardType != null) {
                                Text(
                                    text = "BoardType: ${it.boardType}"
                                )
                            }
                            Text(
                                text = "\n${it.room?.description?.text}",
                            )

                            Button(onClick = {
                                coroutineScope.launch {
                                    val res = Amadeus().bookHotelRooms(
                                        offerId = it.id!!,
                                        adultCount = adultCount.toInt()
                                    )
                                    bookingResponse = if (res.first.isNotEmpty()) {
                                        " Booking Successful. ConfirmationID: ${res.first.first().providerConfirmationId}"
                                    }else{
                                        res.second
                                    }
                                }

                            }) {
                                Text(text = "Book")
                            }
                        }
                    }
                }
            }
        }
    }

    if(bookingResponse.isNotEmpty()){
        AlertDialog(
            title = {
                Text(text = "Booking API Response")
            },
            text = {
                Text(text = bookingResponse)
            },
            onDismissRequest = {
                bookingResponse=""
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bookingResponse=""
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        bookingResponse=""
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DatePickerDemo(state: DatePickerState) {
    DatePicker(
        showModeToggle = false,
        state = state,
        title = null,
        headline = null,
    )
}