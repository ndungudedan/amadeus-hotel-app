import SwiftUI
import shared

struct ContentView: View {
    @State private var selectedAmenities: Set<String> = []
    @State private var selectedHotelRatings: Set<KotlinInt> = []
    @State private var hotelList: [Hotel]=[]
    @State private var errorMessage:String=""
    @State private var selectedCity:String=""
    @State private var isPopoverPresented = false
    
    
    var body: some View {
        NavigationView {
            VStack(alignment: .center) {
                Text("Amadeus Hotel Search")
                    .font(.title)
                    .fontWeight(.bold)
                
                Text("Select Hotel Amenities")
                    .font(.body)
                ScrollView(.horizontal, showsIndicators: false) {
                    LazyHStack(){
                        ForEach(Amadeus().getHotelAmenities(), id: \.self) { amenity in
                            ChipView(title: amenity, isSelected: selectedAmenities.contains(amenity),
                                     onSelected: {
                                if(selectedAmenities.contains(amenity)){
                                    selectedAmenities.remove(amenity)
                                }else{
                                    selectedAmenities.insert(amenity)
                                }
                                if(!selectedCity.isEmpty){
                                    Task {
                                        errorMessage="Loading"
                                        if let res = try? await Amadeus().searchHotel(
                                            city: selectedCity,
                                            amenities: Array(selectedAmenities),
                                            rating: Array(selectedHotelRatings)
                                        ){
                                            if(res.second == nil || res.second!.length == 0){
                                                errorMessage=res.second! as String
                                            }
                                            hotelList=res.first as! [Hotel]
                                        }else{
                                            errorMessage="An error occurred."
                                        }
                                    }
                                }
                            })
                        }
                    }
                    .frame(maxHeight: 40)
                }
                Text("Select Hotel Rating:")
                    .font(.body)
                ScrollView(.horizontal, showsIndicators: false) {
                    LazyHStack{
                        ForEach(Amadeus().getHotelRatings(), id: \.self) { rating in
                            ChipView(title: rating.stringValue, isSelected: selectedHotelRatings.contains(rating),
                                     onSelected: {
                                if(selectedHotelRatings.contains(rating)){
                                    selectedHotelRatings.remove(rating)
                                }else{
                                    selectedHotelRatings.insert(rating)
                                }
                                if(!selectedCity.isEmpty){
                                    Task {
                                        errorMessage="Loading"
                                        if let res = try? await Amadeus().searchHotel(
                                            city: selectedCity,
                                            amenities: Array(selectedAmenities),
                                            rating: Array(selectedHotelRatings)
                                        ){
                                            if(res.second == nil || res.second!.length == 0){
                                                errorMessage=res.second! as String
                                            }
                                            hotelList=res.first as! [Hotel]
                                        }else{
                                            errorMessage="An error occurred."
                                        }
                                    }
                                }
                            })
                        }
                    }                .frame(maxHeight: 40)
                }
                Text("Select a City")
                    .font(.body)
                
                CityView(isPopoverPresented: $isPopoverPresented) { city in
                    selectedCity=city
                    Task {
                        errorMessage="Loading"
                        if let res = try? await Amadeus().searchHotel(
                            city: city,
                            amenities: Array(selectedAmenities),
                            rating: Array(selectedHotelRatings)
                        ){
                            if(res.second == nil || res.second!.length == 0){
                                errorMessage=res.second! as String
                            }
                            hotelList=res.first as! [Hotel]
                        }else{
                            errorMessage="An error occurred."
                        }
                    }
                }
                if(!isPopoverPresented){
                    if(!errorMessage.isEmpty){
                        Text(errorMessage)
                            .font(.body)
                        Spacer()
                    }else{
                        ScrollView( showsIndicators: false) {
                            LazyVStack{
                                ForEach(hotelList, id: \.self) { hotel in
                                    NavigationLink(destination: BookView(hotelId:hotel.hotelId ?? "")) {
                                        HotelCard(hotel: hotel)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .padding(.horizontal, 16)
        }
        .navigationTitle("")
    }
}

struct HotelCard: View {
    let hotel: Hotel
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
            
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(hotel.name ?? "")
                        .font(.title3)
                    
                    HStack {
                        ForEach(0..<Int(truncating: hotel.rating ?? 0), id: \.self) { _ in
                            Image(systemName: "star.fill")
                                .foregroundColor(.yellow)
                        }
                    }
                    
                    if let amenities = hotel.amenities as? [String], !amenities.isEmpty {
                        let amenitiesText = amenities.joined(separator: ", ")
                        Text(amenitiesText)
                            .font(.body)
                    }
                }
                .padding(8)
                
                Spacer()
            }
        }
        .frame(maxWidth: .infinity)
        .background(RoundedRectangle(cornerRadius: 10).stroke(Color.blue, lineWidth: 1))
        .padding(10)
    }
}

struct ChipView: View {
    let title: String
    var isSelected: Bool
    var onSelected: () -> Void
    
    var body: some View {
        HStack(spacing: 4) {
            if(isSelected){
                Image.init(systemName: "checkmark.circle").font(.body)
            }
            Text(title).font(.body).lineLimit(1)
        }
        .padding(.vertical, 4)
        .padding(.leading, 8)
        .padding(.trailing, 8)
        .foregroundColor(isSelected ? .white : .blue)
        .background(isSelected ? Color.blue : Color.white)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(Color.blue, lineWidth: 1.5)
            
        ).onTapGesture {
            onSelected()
        }
    }
}

struct CityView: View {
    @State private var selectedCity = ""
    @State private var searchText = ""
    @Binding var isPopoverPresented: Bool
    let cities = Amadeus().getCityCodes()
    var onSelected: (String) -> Void
    
    var filteredCities: [String] {
        if searchText.isEmpty {
            return Array(cities.keys)
        } else {
            return cities.keys.filter { $0.localizedCaseInsensitiveContains(searchText) }
        }
    }
    
    var body: some View {
        ZStack(alignment: .top){
            VStack {
                TextField("Search", text: $searchText)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .onTapGesture {
                        isPopoverPresented=true
                    }
            }
            .padding()
            .onChange(of: selectedCity) { newValue in
                onSelected(newValue)
            }
            if isPopoverPresented {
                List(cities.keys.filter { searchText.isEmpty || $0.localizedCaseInsensitiveContains(searchText) },
                     id: \.self) { suggestion in
                    Text(suggestion)
                        .onTapGesture {
                            selectedCity = suggestion
                            searchText=suggestion
                            isPopoverPresented=false
                        }
                        .listRowBackground(Color.clear)
                }
                     .frame(maxWidth: 200)
                     .padding(.top,50)
            }
        }
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
