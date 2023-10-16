package com.amadeus.hotel.android

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.amadeus.hotel.Greeting
import com.amadeus.hotel.Hotel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GreetingView()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingView() {
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("No hotels found") }
    val selectedHotelAmenitiesState = remember { mutableStateOf(mutableSetOf<String>()) }
    val selectedHotelRatingState = remember { mutableStateOf(mutableSetOf<Int>()) }
    var selectedCity by remember { mutableStateOf("") }
    var hotelList by remember { mutableStateOf(emptyList<Hotel>())}

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Hotel Search", color = Color.White)
            },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
        )
                )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Select amenities you would like to see")
                AmenitiesChipGroup(
                    selectedHotelAmenities = selectedHotelAmenitiesState.value
                ) {
                    selectedHotelAmenitiesState.value = it
                    coroutineScope.launch {
                        if (selectedCity.isNotEmpty()) {
                            errorMessage="Loading"
                           val res = Greeting().searchHotel(
                                selectedCity,
                                selectedHotelAmenitiesState.value.toList(),
                                selectedHotelRatingState.value.toList()
                            )
                            if(res.second.isNotEmpty()){
                                errorMessage=res.second
                            }
                            hotelList=res.first
                        }
                    }
                }
                Text(text = "Select your preferred hotel rating")
                RatingChipGroup(
                    selectedHotelRating = selectedHotelRatingState.value
                ) {
                    selectedHotelRatingState.value = it
                    coroutineScope.launch {
                        if (selectedCity.isNotEmpty()) {
                            errorMessage="Loading"
                            val res = Greeting().searchHotel(
                                selectedCity,
                                selectedHotelAmenitiesState.value.toList(),
                                selectedHotelRatingState.value.toList()
                            )
                            if(res.second.isNotEmpty()){
                                errorMessage=res.second
                            }
                            hotelList=res.first
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    TextField(
                        value = selectedCity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Select City") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Greeting().getCityCodes().forEach {
                            DropdownMenuItem(text = {
                                Text(text = it.key)
                            }, onClick = {
                                expanded = false
                                selectedCity = it.key
                                coroutineScope.launch {
                                    errorMessage="Loading"
                                    val res = Greeting().searchHotel(
                                        selectedCity,
                                        selectedHotelAmenitiesState.value.toList(),
                                        selectedHotelRatingState.value.toList()
                                    )
                                    if(res.second.isNotEmpty()){
                                        errorMessage=res.second
                                    }
                                    hotelList=res.first
                                }

                            })
                        }
                    }
                }

                if (hotelList.isEmpty()) {
                    Text(text = errorMessage,modifier = Modifier.padding(8.dp))
                }else{
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(hotelList) {
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            shape = MaterialTheme.shapes.medium,
                            //elevation = 5.dp,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(Modifier.padding(8.dp)) {
                                    Text(
                                        text = "${it.name} \t ${it.address?.countryCode?:""} \t ${it.rating?:""}",
                                    )
                                    if(it.amenities.isNotEmpty()){Text(
                                        text = it.amenities.toString(),
                                    )}

                                }
                            }
                        }
                    }
                }
            }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingChipGroup(
    hotelRating: List<Int> = Greeting().getHotelRatings(),
    selectedHotelRating: MutableSet<Int> = mutableSetOf(),
    onSelectedChanged: (MutableSet<Int>) -> Unit = {}
) {
    Column(modifier = Modifier.padding(8.dp)) {
        LazyRow {
            items(hotelRating) { rating ->
                FilterChip(
                    onClick = {
                        val updatedSelectedHotelRating = selectedHotelRating.toMutableSet()
                        if (updatedSelectedHotelRating.contains(rating)) {
                            updatedSelectedHotelRating.remove(rating)
                        } else {
                            updatedSelectedHotelRating.add(rating)
                        }
                        onSelectedChanged(updatedSelectedHotelRating)
                    },
                    label = {
                        Text(rating.toString())
                    },
                    selected = selectedHotelRating.contains(rating),
                    leadingIcon = if (selectedHotelRating.contains(rating)) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmenitiesChipGroup(
    hotelAmenities: List<String> = Greeting().getHotelAmenities(),
    selectedHotelAmenities: MutableSet<String> = mutableSetOf(),
    onSelectedChanged: (MutableSet<String>) -> Unit = {}
) {
    Column(modifier = Modifier.padding(8.dp)) {
        LazyRow {
            items(hotelAmenities) { amenity ->
                FilterChip(
                    onClick = {
                        val updatedSelectedAmenities = selectedHotelAmenities.toMutableSet()
                        if (updatedSelectedAmenities.contains(amenity)) {
                            updatedSelectedAmenities.remove(amenity)
                        } else {
                            updatedSelectedAmenities.add(amenity)
                        }
                        onSelectedChanged(updatedSelectedAmenities)
                    },
                    label = {
                        Text(amenity)
                    },
                    selected = selectedHotelAmenities.contains(amenity),
                    leadingIcon = if (selectedHotelAmenities.contains(amenity)) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView()
    }
}
