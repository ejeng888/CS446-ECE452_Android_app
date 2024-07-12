package com.example.cs446_ece452_android_app.data

import com.example.cs446_ece452_android_app.data.model.Destination
import com.example.cs446_ece452_android_app.data.model.TravelMode
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.CompletableFuture

class MapsApiClient {
    private val client: OkHttpClient = OkHttpClient()
    private val key = "AIzaSyC2E4JsKZl3y3fV0nDUGeZOWfCjrwb5ISw"

    private fun getResponse(request: Request): CompletableFuture<String> {
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

    fun getLatLng(addr: String): CompletableFuture<String> {
        val address = addr.replace(' ', '+')
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=$address&key=$key"

        val request: Request = Request.Builder()
            .url(url)
            .build()

        return getResponse(request)
    }

    fun getRoute(start: Destination, end: Destination, intermediates: ArrayList<Destination>, travelMode: TravelMode): CompletableFuture<String> {
        var stops = ""
        for (dest in intermediates) {
            stops += """
                {
                    "location": {
                        "latLng": {
                            "latitude": ${dest.latLng.latitude},
                            "longitude": ${dest.latLng.longitude}
                        }
                    }
                },
            """
        }

        val requestString = """
            {
                "origin": {
                    "location": {
                        "latLng": {
                            "latitude": ${start.latLng.latitude},
                            "longitude": ${start.latLng.longitude}
                        }
                    }
                },
                "destination": {
                    "location": {
                        "latLng": {
                            "latitude": ${end.latLng.latitude},
                            "longitude": ${end.latLng.longitude}
                        }
                    }
                },
                "intermediates": [
                $stops
                ],
                "travelMode": "$travelMode"
            }"""

        val body = requestString.toRequestBody("application/json".toMediaType())
        val request: Request = Request.Builder()
            .url("https://routes.googleapis.com/directions/v2:computeRoutes")
            .post(body)
            .header("X-Goog-Api-Key", key)
            .header("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline")
            .build()

        return getResponse(request)
    }
}
