package us.mis.acmemobile

import java.util.*

class TripProvider {
    companion object{
        // Crear lista de Trips diferentes
        val trips = listOf<Trip>(
            Trip("0" ,"San Francisco", "Los Angeles", "A trip to the west coast", Calendar.getInstance(), Calendar.getInstance(), 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"),
            Trip("1", "San Francisco", "Los Angeles", "A trip to the west coast", Calendar.getInstance(), Calendar.getInstance(), 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"),
            Trip("2", "San Francisco", "Los Angeles", "A trip to the west coast", Calendar.getInstance(), Calendar.getInstance(), 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"))

    }
}