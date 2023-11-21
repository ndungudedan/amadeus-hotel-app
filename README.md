<a name="readme-top"></a>

<br />
<div align="center">
  <h3 align="center">Amadeus Hotel Booking App Tutorial</h3>

  <p align="center">
    Building a Mobile Hotel Booking App with Kotlin Multi-Platform and Amadeus!
    <br />
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#resources">Resources</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

Android app             |  iOS app
:-------------------------:|:-------------------------:
<img src="https://i.imgur.com/gmCUPkn.gif" width="180" height="350"/>  |  <img src="https://i.imgur.com/F3LJ2zf.gif" width="180" height="350"/>

This tutorial teaches you how to create a mobile hotel booking app in Kotlin Multi-Platform. You will use the Amadeus Hotel Search APIs to search for available hotels within major worldwide cities and complete the booking flow using the Amadeus Hotel Booking API.
This tutorial will be in two parts:
1. Part 1 teaches you how to search and display hotels within a given city.
2. The final part shows you how to search and book available hotel rooms.

### Built With

This tutorial uses the following frameworks/libraries:

* [Android Studio](https://developer.android.com/studio)
* [Xcode](https://developer.apple.com/xcode/)
* [Kotlin](https://kotlinlang.org/)
* [Swift](https://www.swift.org/)
* [Kotlin Multi-Platform](https://kotlinlang.org/docs/multiplatform.html)

<!-- GETTING STARTED -->
## Getting Started

To get started with this tutorial, you will need to do the following:

1. Clone the repo
   ```sh
   git clone https://github.com/ndungudedan/amadeus-hotel-app
   ```
  If you are on part 1 of the tutorial, please checkout the `part-1` branch
  ```sh
   git checkout part-1
   ```
  However, if you would like to see the complete code for this 2-part series, please checkout the `main` branch:
  ```sh
   git checkout main
   ```
2. Create an [Amadeus developer account](https://developers.amadeus.com/register).
3. To get your API keys, Create a new app on the [Amadeus developer dashboard](https://developers.amadeus.com/my-apps).
4. Enter your API keys in `shared/src/commonMain/kotlin/com/amadeus/hotel/AmadeusApi.kt`
   ```kotlin
   append("client_id", "your-client-id")
   append("client_secret", "your-client-secret")
   ```
5. Open, sync, and build your project on Android Studio.
6. Open the `iosApp` folder on XCode to build and run the iOS version.

<!-- USAGE EXAMPLES -->
## Usage

Please refer to these documentation guides for further understanding of the Amadeus API:

- [Authorizing API requests](https://amadeus4dev.github.io/developer-guides/API-Keys/authorization/#requesting-an-access-token)
- [Listing hotels](https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-list/api-reference)
- [Search for hotel rooms](https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-search/api-reference)
- [Book a hotel room](https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-booking/api-reference)

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<!-- ACKNOWLEDGMENTS -->
## Resources

* [Kotlin Multi-Platform](https://kotlinlang.org/docs/multiplatform.html)
* [Ktor Making API requests](https://ktor.io/docs/client-dependencies.html)
* [JetPack Compose](https://developer.android.com/jetpack/compose/documentation)
* [SwiftUI](https://developer.apple.com/documentation/swiftui/)
* [JSON to Kotlin Data Class Converter](https://json2kt.com/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
