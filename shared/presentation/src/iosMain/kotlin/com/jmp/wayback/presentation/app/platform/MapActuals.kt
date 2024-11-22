package com.jmp.wayback.presentation.app.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKMapItem
import platform.MapKit.MKPlacemark

@OptIn(ExperimentalForeignApi::class)
actual fun openMap(latitude: Double, longitude: Double) {
    val coordinate = CLLocationCoordinate2DMake(latitude, longitude)
    val placemark = MKPlacemark(coordinate)
    val mapItem = MKMapItem(placemark = placemark)
    mapItem.name = "Wayback pin"
    mapItem.openInMapsWithLaunchOptions(null)
}
