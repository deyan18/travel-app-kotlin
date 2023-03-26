package us.mis.acmemobile

import java.util.*

class TripProvider {
    companion object{
        // Crear lista de Trips diferentes
        val tripList = listOf<Trip>(
            Trip("San Francisco", "Los Angeles", "A trip to the west coast", Calendar.getInstance(), Calendar.getInstance(), 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"),
            Trip("San Francisco", "Los Angeles", "A trip to the west coast", Calendar.getInstance(), Calendar.getInstance(), 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"),
            Trip("San Francisco", "Los Angeles", "A trip to the west coast", Calendar.getInstance(), Calendar.getInstance(), 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"))

    }
}