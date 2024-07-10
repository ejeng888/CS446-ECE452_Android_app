package com.example.cs446_ece452_android_app.data

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class MapsService : Service() {

    private val binder = LocalBinder()
    private var client: OkHttpClient = OkHttpClient()
    private val key = "AIzaSyC2E4JsKZl3y3fV0nDUGeZOWfCjrwb5ISw"

    inner class LocalBinder : Binder() {
        fun getService(): MapsService = this@MapsService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun getResponse(request: Request): Future<String> {
        val result: CompletableFuture<String> = CompletableFuture()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    result.complete(responseData)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        return result
    }

    fun getLatLng(addr: String): Future<String> {
        val address = addr.replace(' ', '+')
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=$key"

        val request: Request = Request.Builder()
            .url(url)
            .build()

        return getResponse(request)
    }

    fun getRoute() {
        val string = """
            {
                "origin": {
                    "location": {
                        "latLng": {
                            "latitude": 34.9938,
                            "longitude": 135.7716
                        }
                    }
                },
                "destination": {
                    "location": {
                        "latLng": {
                            "latitude": 34.9938,
                            "longitude": 135.7716
                        }
                    }
                },
                "intermediates": [
                    {
                        "address": "68 Fukakusa Yabunouchicho, Fushimi Ward, Kyoto, 612-0882, Japan"
                    },
                    {
                        "address": "1 Chome-294 Kiyomizu, Higashiyama Ward, Kyoto, 605-0862, Japan"
                    },
                    {
                        "address": "48 Eikandocho, Sakyo Ward, Kyoto, 606-8445, Japan"
                    },
                    {
                        "address": "1 Kinkakujicho, Kita Ward, Kyoto, 603-8361, Japan"
                    },
                    {
                        "address": "Nakagyo Ward, Kyoto, 604-8055, Japan"
                    },
                ],
                "travelMode": "DRIVE"
            }"""

        val body = string.toRequestBody("application/json".toMediaType())
        val request: Request = Request.Builder()
            .url("https://routes.googleapis.com/directions/v2:computeRoutes")
            .post(body)
            .header("X-Goog-Api-Key", key)
            .header("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline")
            .build()

        getResponse(request)
    }
}
