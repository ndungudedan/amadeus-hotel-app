import SwiftUI
import shared

struct ContentView: View {
    @State private var selectedAmenities: Set<String> = []

    var body: some View {
         VStack(spacing: 16) {
             Text("Amadeus")
                 .font(.title)
                 .fontWeight(.bold)
             ScrollView {
                 
                 HStack(spacing: 16) {
                     ScrollView {
                         ForEach(Greeting().getHotelAmenities(), id: \.self) { amenity in
                                 VStack {
                                     ChipView(title: amenity, isSelected: selectedAmenities.contains(amenity),
                                              onSelected: {
                                         if(selectedAmenities.contains(amenity)){
                                             selectedAmenities.remove(amenity)
                                         }else{
                                             selectedAmenities.remove(amenity)
                                         }
                                     })
                                     Divider()
                                 }
                             }
                     }
                     Spacer()
                 }
                 
             }
             Spacer()
         }
         .padding(.horizontal, 16)
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
        .padding(.leading, 4)
        .padding(.trailing, 10)
        .foregroundColor(isSelected ? .white : .blue)
        .background(isSelected ? Color.blue : Color.white)
        .cornerRadius(20)
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(Color.blue, lineWidth: 1.5)

        ).onTapGesture {
            
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
