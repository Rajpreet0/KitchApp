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

    // Function to Login
    fun login(email: String, password: String): JsonObject {
        val json = JsonObject().apply {
            addProperty("email", email);
            addProperty("password", password);
        }.toString()


        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)


        val request = Request.Builder()
            .url(SERVER_ADDRESS_LOGIN)
            .post(requestBody)
            .build()


        // Execute the request and use the response
        client.newCall(request).execute().use { response ->
            // Throw an IOException if the request was not successful
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            // Get the response body as a string
            val responseData = response.body!!.string()
            // Return JSON Object for the User Data
            return Gson().fromJson(responseData, JsonObject::class.java)
        }
    }

    companion object {
        const val SERVER_ADDRESS_OPENAI = "https://kitch-app-server.vercel.app"
        const val SERVER_ADDRESS_LOGIN = "https://kitch-app-server.vercel.app/users/login"
    }
}