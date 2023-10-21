import SwiftUI
import shared

struct ContentView: View {
    @State private var selectedAmenities: Set<String> = []
    @State private var selectedHotelRatings: Set<KotlinInt> = []
    @State private var hotelList: [Hotel]=[]
    @State private var errorMessage:String=""
    @State private var selectedCity:String=""
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Amadeus")
                .font(.title)
                .fontWeight(.bold)
            
            Text("Select Hotel Amenities")
                .font(.body)
            ScrollView(.horizontal, showsIndicators: false) {
                LazyHStack(){
                    ForEach(Greeting().getHotelAmenities(), id: \.self) { amenity in
                        ChipView(title: amenity, isSelected: selectedAmenities.contains(amenity),
                                 onSelected: {
                            if(selectedAmenities.contains(amenity)){
                                selectedAmenities.remove(amenity)
                            }else{
                                selectedAmenities.insert(amenity)
                            }
                            Task {
                                errorMessage="Loading"
                                if let res = try? await Greeting().searchHotel(
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
                        })
                    }
                }
                .frame(maxHeight: 40)
            }
            Text("Select Hotel Rating:")
                .font(.body)
            ScrollView(.horizontal, showsIndicators: false) {
                LazyHStack{
                    ForEach(Greeting().getHotelRatings(), id: \.self) { rating in
                        ChipView(title: rating.stringValue, isSelected: selectedHotelRatings.contains(rating),
                                 onSelected: {
                            if(selectedHotelRatings.contains(rating)){
                                selectedHotelRatings.remove(rating)
                            }else{
                                selectedHotelRatings.insert(rating)
                            }
                            Task {
                                errorMessage="Loading"
                                if let res = try? await Greeting().searchHotel(
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
                        })
                    }
                }                .frame(maxHeight: 40)
            }
            Text("Select a City")
                .font(.body)
            CityView() { city in
                selectedCity=city
                Task {
                    errorMessage="Loading"
                    if let res = try? await Greeting().searchHotel(
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
            
            ScrollView( showsIndicators: false) {
                LazyVStack{
                    ForEach(hotelList, id: \.self) { hotel in
                        HotelCard(hotel: hotel)
                    }
                }
            }
            
        }
        .padding(.horizontal, 16)
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
                        Text(hotel.address?.countryCode ?? "")
                            .font(.subheadline)
                        
                        Spacer()
                        
                        Text("\(hotel.rating ?? 0)")
                            .font(.subheadline)
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
    let cities = Greeting().getCityCodes()
    var onSelected: (String) -> Void
    
    var filteredCities: [String] {
        if searchText.isEmpty {
            return Array(cities.keys)
        } else {
            return cities.keys.filter { $0.localizedCaseInsensitiveContains(searchText) }
        }
    }
    
    var body: some View {
        VStack {
            TextField("Search", text: $searchText)
                .textFieldStyle(RoundedBorderTextFieldStyle())
            
            Picker("City", selection: $selectedCity) {
                ForEach(filteredCities, id: \.self) { city in
                    Text(city).tag(city)
                }
            }
            .pickerStyle(MenuPickerStyle())
            
        }
        .padding()
        .onChange(of: selectedCity) { newValue in
            onSelected(newValue)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
