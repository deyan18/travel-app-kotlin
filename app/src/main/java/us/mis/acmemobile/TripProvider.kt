package us.mis.acmemobile

import java.util.*

class TripProvider {
    companion object{
        // Crear lista de Trips diferentes
        val trips = listOf<Trip>(
            Trip("0" ,"San Francisco", "Los Angeles", "A trip to the west coast", Calendar.getInstance().apply { set(2023, 5, 4) }, Calendar.getInstance().apply { set(2023, 5, 10) }, 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"),
            Trip("1", "Almeria", "Malaga", "A trip to the west coast", Calendar.getInstance().apply { set(2023, 4, 4) }, Calendar.getInstance().apply { set(2023, 4, 10) }, 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"),
            Trip("2", "San Francisco", "Sofia", "A trip to the west coast", Calendar.getInstance().apply { set(2023, 6, 4) }, Calendar.getInstance().apply { set(2023, 6, 6) }, 100.0, false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/elle-los-angeles02-1559906859.jpg"))

    }
}