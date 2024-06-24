package com.example.cs446_ece452_android_app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.AdvancedMarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PinConfig
import com.google.android.gms.maps.model.PolylineOptions
@Composable
fun MapScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Use the provided padding values
        ) {
            val mapView = rememberMapViewWithLifecycle()
            AndroidView({ mapView }) { mapView ->
                mapView.getMapAsync { map ->
                    val encodedPolyline =
                        "gvqtEw{t{XlDXzFv@h@FpDGnGJEx@KpH?nGxCQ|CO|CBf@HvNsAdFU^@pB\\x@Ad@BtCl@lAL~@?vGi@hBIzBBnCLtCf@fCl@l@LvAJ|A@|AS`Bq@n@[RUVMpAK|AA`d@FvRBR@TyOaI@wEHa@?WGUBi@PuBDC_CBYCy@RCCuAYBDtAB?B|@Eh@DjBsE?mG?eDEoCLiB@}BD}HLiIAgC?gC?{L^oANkCHiBL}@BKyBS}ABEFAa@y@gGqIy@m@[MeHk@iKSaJo@aIYeRgAo@WgFoDe@a@m@a@BYQkA_AaDqA{BY{@e@mBiAmDQ_AtCc@uCb@\\zAhAlDd@pBXn@jArB|@bDJp@HEJm@r@qAd@sAd@iB`@yCCg@y@aBKs@OuB?Q]}BOk@Gi@Dk@G[a@eAIUZ|@Tl@@ZE^N~@Nh@TpBLjBHv@L`@p@rA@f@g@bDk@vBi@lAc@v@GXETGB?\\AFWOq@OmP}BuJaAoGo@oFX{DbAc@?uBUkBo@oC_@wZeBE_EP}DXcGDwEo@iNK{CN_F`@cJNcBHc@Ik@KKECoDtBwDnCUNUk@eA?kB?_@M[Wa@c@aA_AmAqAaBmBoB}AY_@Ba@B{AEyCGk@EQCsG}ANeBLyDL}BNz@bGHfALbET?F`E?zEQpGeB~S]`JCvX]r[AdP?rDHdDkNkAsCAqEVmCR_SGsEOeIc@iAIyB_@iBOoB@cEb@YAQEYQqEjCkLxGuNpIwBlAMDEFqAt@qCbBoLrGoBdAcEnBw@NGNgAj@\\h\\Dj@Rp@tChGfHjOdAvBTl@Lz@?vDE~CBbMDjIAtAe@pFg@dF_@xE@jAbAnSvAnYL|@N`@ZZ^NpBd@z@P]dFEfAB?BvAPpC?z@Md@I|@GPGZm@Po@AEG?_@?`@HFp@Ah@QHg@BGLgAHW?{@SkDA}@C?DgA\\eFD?l@oJDQdIgL~BsGzAcFHe@CWKi@dEiBlA]jVwElASbDSD{@QgEIsILiNHyKHcSB}IxBoMAgLrBAPIva@FvX?zEEr@Wr@GvMM~FIfICxG@lJBvRC~ICCgU?mCgG@?qA?gBdGACeMIgLKuN?q@jALzA^tBp@xCnAvDbBbJjDbLpDbF~ARkDdAiNhFPt@F"
                    val decoded = decodePolyline(encodedPolyline)
                    val polylineOptions = PolylineOptions()
                    for (item in decoded)
                        polylineOptions.add(item)
                    map.addPolyline(polylineOptions)

                    val start = LatLng(34.9939, 135.7714)
                    map.addMarker(
                        MarkerOptions()
                            .position(start)
                    )

                    val stops = listOf(
                        "Fushimi Inari Shrine" to LatLng(34.9672, 135.7727),
                        "Kiyomizu-dera" to LatLng(34.9953, 135.7811),
                        "Eikando Temple" to LatLng(35.0147, 135.7939),
                        "Kinkaku-ji" to LatLng(35.0391, 135.7297),
                        "Nishiki Market" to LatLng(35.0050, 135.7648)
                    )

                    val pinConfigBuilder = PinConfig.builder()
                    pinConfigBuilder.setBackgroundColor(Color.MAGENTA)
                        .setBorderColor(Color.MAGENTA)

                    val advancedMarkerAvailable = map.mapCapabilities.isAdvancedMarkersAvailable
                    if (advancedMarkerAvailable) {
                        for ((index, stop) in stops.withIndex()) {
                            pinConfigBuilder.setGlyph(PinConfig.Glyph((index + 1).toString(), Color.WHITE))
                            val pinConfig = pinConfigBuilder.build()

                            map.addMarker(
                                AdvancedMarkerOptions()
                                    .position(stop.second)
                                    .title(stop.first)
                                    .icon(BitmapDescriptorFactory.fromPinConfig(pinConfig))
                            )
                        }
                    } else {
                        for (stop in stops) {
                            map.addMarker(
                                MarkerOptions()
                                    .position(stop.second)
                                    .title(stop.first)
                            )
                        }
                    }

                    val center = LatLng(35.0048, 135.7685)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12f))
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                mapView.onCreate(Bundle())
            }

            override fun onStart(owner: LifecycleOwner) {
                mapView.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                mapView.onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                mapView.onDestroy()
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}

private fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val finalLat = lat / 1E5
        val finalLng = lng / 1E5
        poly.add(LatLng(finalLat, finalLng))
    }

    return poly
}
