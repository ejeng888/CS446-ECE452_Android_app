package com.example.cs446_ece452_android_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

//    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_google_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        val encodedPolyline = "gvqtEw{t{XlDXzFv@_@zHeIYcH_@iAIo@xHQjAOxB?rAg@dKYpNOnMAvJG~MCT@`J?tHMzN?`DLpLLxQBrGFhE?nJFhWAnHMnEQvE?Zmq@n@iQD_eAHm@DyFCsFBkJXiKVgGNii@@qAUsWsFGjA?~JaHkGi@Ko@GBvAPpC?z@Md@I|@GPGZm@Po@AEG?_@?`@HFp@Ah@QHg@BGLgAHW?{@SkDA}@C?DgA\\eFfe@xJlVA~IADa@\\_KCi@Ic@cDsKe@Nm@?MTc@F{EIu@C@c@nEFrCJXCVKeGoSOw@EeCCuC_@_HD{@QgEIsILiNHyKHcSB}IxBoMAgLrBAPIva@FvX?zEEHANURq@Cen@]aP?kPDwHXcVBwX\\wJdBiSPqGAaIE{AnBKpDa@j@Dh@Xh@j@Ba@B{AEyCGk@EQCsG}ANeBLyDL}BNz@bGHfALbET?F`E?zEQpGeB~S]`JAzVfVHvJGlXzAvP`AvB\\fBl@nCVHAzDcAnFYnGn@tJ`A|PbC`@HVNBYQkA_AaDqA{BY{@e@mBiAmDQ_AtCc@uCb@\\zAhAlDd@pBXn@jArB|@bDJp@HEJm@r@qAd@sAd@iB`@yCCg@y@aBKs@OuB?Q]}BOk@Gi@Dk@G[a@eAIUZ|@Tl@@ZE^N~@Nh@TpBLjBHv@L`@p@rA@f@g@bDk@vBi@lAc@v@GXETGB?\\AF`Ap@PPWbAcAdI_A|FIP[|ClEPd@D"
        val decoded = decodePolyline(encodedPolyline)
        val polylineOptions = PolylineOptions()
        for (item in decoded)
            polylineOptions.add((item))
        map.addPolyline(polylineOptions)

        val center = LatLng(35.0140, 135.7483)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12f))
    }

    private fun decodePolyline(encoded: String) : List<LatLng> {
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


//    fun getResponse(url: String) : String? {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        val response = client.newCall(request).execute()
//
//        return response.body()?.string()
//    }
}