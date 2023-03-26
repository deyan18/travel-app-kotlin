package us.mis.acmemobile

import java.util.Calendar

data class Trip (val origin: String, val destination: String, val description: String, val startDate: Calendar, val endDate: Calendar, val price: Double, val boomarked: Boolean, val imgURL: String)