package com.abhimanyu.dogsitting.mobile.ui.navigation


import androidx.annotation.DrawableRes
import com.abhimanyu.dogsitting.mobile.R

data class BottomNavItem (val route: String,
                          val label: String,
                          @DrawableRes val iconRes: Int)
object BottomNavItems {
    const val ROUTE_BOOKINGS = "booking_list"
    const val ROUTE_DASHBOARD = "dashboard"

    val bottomRoutes = setOf(ROUTE_BOOKINGS, ROUTE_DASHBOARD)
    /*
    val items = listOf(
        BottomNavItem(
            route = ROUTE_BOOKINGS,
            label = "Bookings",
            iconRes = R.drawable.ic_bookings // we’ll create these icons in a second
        ),
        BottomNavItem(
            route = ROUTE_DASHBOARD,
            label = "Dashboard",
            iconRes = R.drawable.ic_dashboard
        )
    )

     */
}