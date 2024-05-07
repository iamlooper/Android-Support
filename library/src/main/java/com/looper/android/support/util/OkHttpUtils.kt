package com.looper.android.support.util

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpUtils {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    /**
     * Sends a GET request synchronously and returns a Call object that can be used to execute the request.
     *
     * @param url The URL to send the GET request to.
     * @throws IOException If an error occurs during the HTTP request.
     * @return A Call object representing the HTTP request.
     */
    @Throws(IOException::class, Exception::class)
    fun sendGetRequest(url: String): Call {
        val request = Request.Builder()
            .url(url)
            .build()

        return client.newCall(request)
    }

    /**
     * Sends a GET request asynchronously and invokes the provided Callback upon completion or failure.
     *
     * @param url The URL to send the GET request to.
     * @param callback The callback to be invoked upon completion or failure.
     */
    fun sendAsyncGetRequest(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    /**
     * Sends a POST request synchronously with a JSON body and returns a Call object.
     *
     * @param url The URL to send the POST request to.
     * @param jsonBody The JSON body of the POST request.
     * @throws IOException If an error occurs during the HTTP request.
     * @return A Call object representing the HTTP request.
     */
    @Throws(IOException::class, Exception::class)
    fun sendPostRequest(url: String, jsonBody: JSONObject): Call {
        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        return client.newCall(request)
    }

    /**
     * Sends a POST request asynchronously with a JSON body and invokes the provided Callback.
     *
     * @param url The URL to send the POST request to.
     * @param jsonBody The JSON body of the POST request.
     * @param callback The callback to be invoked upon completion or failure.
     */
    fun sendAsyncPostRequest(url: String, jsonBody: JSONObject, callback: Callback): Call {
        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    /**
     * Cancels a specific HTTP request identified by its Call object.
     *
     * @param call The Call object representing the HTTP request to cancel.
     */
    fun stopRequest(call: Call) {
        call.cancel()
    }

    /**
     * Cancels all pending HTTP requests associated with the OkHttpClient instance.
     */
    fun stopRequests() {
        client.dispatcher.cancelAll()
    }
}