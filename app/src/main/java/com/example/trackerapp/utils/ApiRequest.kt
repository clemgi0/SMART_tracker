package com.example.trackerapp.utils

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

fun get(url: String, client: OkHttpClient) : String? {
    val request = Request.Builder()
        .url(url)
        .build()

    var body: String? = null
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {throw Error(e.message)}
        override fun onResponse(call: Call, response: Response) {
            body = response.body?.string()
        }
    })

    return body
}

fun post(url: String, json: String, client: OkHttpClient) {
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            throw Error(e.message)
        }

        override fun onResponse(call: Call, response: Response) {
            // Handle response, possibly on the UI thread
            println(response.body?.string())
        }
    })
}