package com.example.cs446_ece452_android_app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(navController: NavController) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val mapId = stringResource(R.string.map_id)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Use the provided padding values
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(35.0048, 135.7685), 12.5f)
                },
                googleMapOptionsFactory = {
                    GoogleMapOptions().mapId(mapId)
                },
                onMapLoaded = { isMapLoaded = true },
                content = {
                    StartEndMarker()
                    StopMarkers()
                    Route()
                }
            )
        }
    }
}

@Composable
@GoogleMapComposable
fun StartEndMarker() {
    val pos = LatLng(34.9939, 135.7714)

    return Marker(
        state = rememberMarkerState(position = pos),
    )
}

@Composable
@GoogleMapComposable
fun StopMarkers() {
    val stops = ArrayList<Pair<String, LatLng>>()
    stops.add(Pair("Fushimi Inari Shrine", LatLng(34.9672, 135.7727)))
    stops.add(Pair("Kiyomizu-dera", LatLng(34.9953, 135.7811)))
    stops.add(Pair("Eikando Temple", LatLng(35.0147, 135.7939)))
    stops.add(Pair("Kinkaku-ji", LatLng(35.0391, 135.7297)))
    stops.add(Pair("Nishiki Market", LatLng(35.0050, 135.7648)))

    return stops.forEachIndexed() { i, stop ->
        AdvancedMarker(
            state = rememberMarkerState(position = stop.second),
            title = stop.first,
            pinConfig = configPin((i + 1).toString())
        )
    }
}

fun configPin(s: String): PinConfig {
    return with(PinConfig.builder()) {
        setGlyph(PinConfig.Glyph(s, android.graphics.Color.WHITE))
        setBackgroundColor(android.graphics.Color.MAGENTA)
        setBorderColor(android.graphics.Color.MAGENTA)
        build()
    }
}

@Composable
@GoogleMapComposable
fun Route() {
    val encodedPolyline =
        "gvqtEw{t{XlDXzFv@h@FpDGnGJEx@KpH?nGxCQ|CO|CBf@HvNsAdFU^@pB\\x@Ad@BtCl@lAL~@?vGi@hBIzBBnCLtCf@fCl@l@LvAJ|A@|AS`Bq@n@[RUVMpAK|AA`d@FvRBR@TyOaI@wEHa@?WGUBi@PuBDC_CBYCy@RCCuAYBDtAB?B|@Eh@DjBsE?mG?eDEoCLiB@}BD}HLiIAgC?gC?{L^oANkCHiBL}@BKyBS}ABEFAa@y@gGqIy@m@[MeHk@iKSaJo@aIYeRgAo@WgFoDe@a@m@a@BYQkA_AaDqA{BY{@e@mBiAmDQ_AtCc@uCb@\\zAhAlDd@pBXn@jArB|@bDJp@HEJm@r@qAd@sAd@iB`@yCCg@y@aBKs@OuB?Q]}BOk@Gi@Dk@G[a@eAIUZ|@Tl@@ZE^N~@Nh@TpBLjBHv@L`@p@rA@f@g@bDk@vBi@lAc@v@GXETGB?\\AFWOq@OmP}BuJaAoGo@oFX{DbAc@?uBUkBo@oC_@wZeBE_EP}DXcGDwEo@iNK{CN_F`@cJNcBHc@Ik@KKECoDtBwDnCUNUk@eA?kB?_@M[Wa@c@aA_AmAqAaBmBoB}AY_@Ba@B{AEyCGk@EQCsG}ANeBLyDL}BNz@bGHfALbET?F`E?zEQpGeB~S]`JCvX]r[AdP?rDHdDkNkAsCAqEVmCR_SGsEOeIc@iAIyB_@iBOoB@cEb@YAQEYQqEjCkLxGuNpIwBlAMDEFqAt@qCbBoLrGoBdAcEnBw@NGNgAj@\\h\\Dj@Rp@tChGfHjOdAvBTl@Lz@?vDE~CBbMDjIAtAe@pFg@dF_@xE@jAbAnSvAnYL|@N`@ZZ^NpBd@z@P]dFEfAB?BvAPpC?z@Md@I|@GPGZm@Po@AEG?_@?`@HFp@Ah@QHg@BGLgAHW?{@SkDA}@C?DgA\\eFD?l@oJDQdIgL~BsGzAcFHe@CWKi@dEiBlA]jVwElASbDSD{@QgEIsILiNHyKHcSB}IxBoMAgLrBAPIva@FvX?zEEr@Wr@GvMM~FIfICxG@lJBvRC~ICCgU?mCgG@?qA?gBdGACeMIgLKuN?q@jALzA^tBp@xCnAvDbBbJjDbLpDbF~ARkDdAiNhFPt@F"
    val decoded = decodePolyline(encodedPolyline)

    return Polyline(
        points = decoded,
        color = Color.Blue
    )
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