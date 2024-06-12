package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class NetworkHelper {
    private val client = OkHttpClient()

    fun sendOpenAIRequest(query: String): String {
        val json = JsonObject().apply {
            addProperty("query", query)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        // Create a new request to the specified server address
        val request = Request.Builder()
            .url(SERVER_ADDRESS_OPENAI)
            .post(requestBody)
            .build()

        // Execute the request and use the response
        client.newCall(request).execute().use { response ->
            // Throw an IOException if the request was not successful
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            // Get the response body as a string
            val responseData = response.body!!.string()
            // Parse the response JSON string into a JsonObject using Gson
            val jsonObject = Gson().fromJson(responseData, JsonObject::class.java)
            // Return the "reply" field from the JsonObject as a String
            return jsonObject.get("reply").asString
        }
    }


    companion object {
        const val SERVER_ADDRESS_OPENAI = "http://10.0.2.2:3000"
    }
}