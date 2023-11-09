import SwiftUI
import shared

struct BookView:View{
    let hotelId: String
    @State private var checkInDate = Date()
    @State private var checkOutDate = Date()
    @State private var hotelOffers: HotelOffers?
    @State private var errorMessage:String="No Hotel Rooms Found"
    @State private var bookingResponse:String=""
    @State private var adultCount = "1"
    @State private var showAlert = false
    let dateFormatter = DateFormatter()
    
    var body: some View {
        VStack() {
            DatePicker("Check In",
                       selection: $checkInDate,
                       displayedComponents: [.date]
            ).padding(.horizontal,20)
            DatePicker("Check Out",
                       selection: $checkOutDate,
                       displayedComponents: [.date]
            ).padding(.horizontal,20)
            
            Text("No. of Adults:")
                .font(.headline)
                .padding(.bottom, 5)
            TextField(
                "No. of Adults",
                text: $adultCount
            )
            .textFieldStyle(RoundedBorderTextFieldStyle())
            Button(action: {
                Task {
                    hotelOffers=nil
                    errorMessage="Loading"
                    dateFormatter.dateFormat = "yyyy-MM-dd"
                    if let res = try? await Amadeus().searchHotelOffers(
                        hotelIds: hotelId,
                        checkInDate: dateFormatter.string(from:checkInDate),
                        checkOutDate: dateFormatter.string(from: checkOutDate), adults: adultCount
                    ){
                        if(res.second == nil || res.second!.length == 0){
                            errorMessage=res.second! as String
                        }
                        if(res.first!.count > 0){
                            hotelOffers=res.first![0] as? HotelOffers
                        }else{
                            errorMessage="No Hotel Rooms Found"
                        }
                    }else{
                        errorMessage="An error occurred."
                    }
                }
            }) {
                Text("Search")
            }.padding(8)
            
            if let hotelOffers = hotelOffers {
                Text("\(hotelOffers.hotel?.name ?? "")")
                    .font(.body)
                ScrollView(showsIndicators: false) {
                    LazyVStack {
                        ForEach(hotelOffers.offers as? [Offers] ?? [], id: \.self) { offer in
                            ZStack {
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(Color.white)
                                VStack(alignment: .leading, spacing: 4) {
                                    
                                    Text("Price: \(offer.price?.currency ?? "") \(offer.price?.total ?? "") @\(offer.price?.variations?.average?.base ?? "") \((offer.price!.taxes[0] as! Taxes).pricingFrequency ?? "")").font(.body)
                                    Text("Payment Type: \(offer.policies?.paymentType ?? "")")
                                        .font(.body)
                                    if(offer.boardType != nil){
                                        Text("BoardType: \(offer.boardType!)").font(.body)
                                    }
                                    Text("\n\(offer.room?.description_?.text ?? "")").font(.body)
                                    Button(action: {
                                        Task {
                                            if let res = try? await Amadeus().bookHotelRooms(offerId: offer.id!){
                                                if(res.first!.count > 0){
                                                    bookingResponse="Booking Successful. ConfirmationID: \((res.first![0] as! BookingData).providerConfirmationId ?? "")"
                                                }else{
                                                    bookingResponse=res.second! as String
                                                }
                                            }else{
                                                bookingResponse="An error occurred."
                                            }
                                            showAlert=true
                                        }
                                    }) {
                                        Text("Book")
                                    }.padding(8)
                                }
                                .padding(8)
                                
                            }
                            .frame(maxWidth: .infinity)
                            .background(RoundedRectangle(cornerRadius: 10).stroke(Color.blue, lineWidth: 1))
                            .padding(10)
                        }
                    }
                }
            }else{
                Text(errorMessage)
                    .padding(8)
                    .font(.body)
                Spacer()
            }
            
        }.navigationBarTitle("Book Hotel Room")
            .alert(isPresented: $showAlert) {
                Alert(
                    title: Text("Booking API Response"),
                    message: Text(bookingResponse),
                    dismissButton: .default(Text("OK"))
                )
            }
    }
}
