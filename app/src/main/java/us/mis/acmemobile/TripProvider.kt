package us.mis.acmemobile

import android.icu.text.DecimalFormat
import java.util.*

class TripProvider {
    companion object {
        // Crear lista de Trips diferentes
        val trips = mutableListOf<Trip>()

        fun generateRandomTrip(): Trip {
            val cities = listOf("San Francisco", "New York", "Almeria", "Seoul", "Sofia", "Budapest", "Tokyo", "Paris")
            val cityImages = mapOf(
                "San Francisco" to "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg",
                "New York" to "https://media.timeout.com/images/105124812/image.jpg",
                "Almeria" to "https://media.traveler.es/photos/61375dc1bae07f0d8a49206d/master/w_1600%2Cc_limit/209774.jpg",
                "Seoul" to "https://media.cntraveler.com/photos/6123f6bb7dfe5dff926c7675/3:2/w_2529,h_1686,c_limit/South%20Korea_GettyImages-1200320719.jpg",
                "Sofia" to "https://planetofhotels.com/guide/sites/default/files/styles/paragraph__live_banner__lb_image__1880bp/public/live_banner/sofia-1.jpg",
                "Budapest" to "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/budapest-danubio-parlamento-1552491234.jpg",
                "Tokyo" to "https://planetofhotels.com/guide/sites/default/files/styles/node__blog_post__bp_banner/public/live_banner/Tokyo.jpg",
                "Paris" to "https://elpachinko.com/wp-content/uploads/2019/03/10-lugares-imprescindibles-que-visitar-en-Par%C3%ADs.jpg"

                )
            val random = Random()

            var fromCity = ""
            var toCity = ""

            // Generar dos ciudades distintas
            while (fromCity == toCity) {
                fromCity = cities[random.nextInt(cities.size)]
                toCity = cities[random.nextInt(cities.size)]
            }

            // Generar fechas aleatorias
            val today = Calendar.getInstance()
            val departureDate = Calendar.getInstance()
            departureDate.set(Calendar.YEAR, today.get(Calendar.YEAR))
            departureDate.set(Calendar.DAY_OF_YEAR, random.nextInt(365 - today.get(Calendar.DAY_OF_YEAR)) + today.get(Calendar.DAY_OF_YEAR))

            val returnDate = Calendar.getInstance()
            returnDate.set(Calendar.YEAR, today.get(Calendar.YEAR))
            returnDate.set(Calendar.DAY_OF_YEAR, departureDate.get(Calendar.DAY_OF_YEAR) + random.nextInt(7) + 1)


            // Crear y retornar el nuevo Trip
            return Trip(
                UUID.randomUUID().toString(),
                fromCity,
                toCity,
                "A trip to $toCity. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc scelerisque neque in risus aliquet laoreet. Nulla quis dapibus velit. Proin eu dolor ipsum. Morbi et orci a tortor luctus feugiat at quis dolor. Nullam vel mi leo. Fusce justo eros, accumsan id aliquam ac, pellentesque nec neque.",
                departureDate,
                returnDate,
                (random.nextDouble() * 1000).toInt().toDouble(), //To fix an issue on slider without changing all the code...
                cityImages[toCity] ?: ""
            )
        }

        init {
            repeat(10) {
                var newTrip = generateRandomTrip()
                trips.add(newTrip)
            }
        }


    }
}
