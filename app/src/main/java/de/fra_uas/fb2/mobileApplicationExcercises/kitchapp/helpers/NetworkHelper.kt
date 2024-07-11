package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers

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

    fun register(username: String, email: String, password: String): JsonObject {
        val json = JsonObject().apply {
            addProperty("username",username);
            addProperty("email", email);
            addProperty("password", password);
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_REGISTER)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            return Gson().fromJson(responseData, JsonObject::class.java)
        }
    }

    fun deleteUser(email: String): String {
        val json = JsonObject().apply {
            addProperty("email", email);
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_DELETE_USER)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            val jsonObject = Gson().fromJson(responseData, JsonObject::class.java)

            return jsonObject.get("message").asString
        }
    }

    fun forgotPassword(email: String): String {
        val json = JsonObject().apply {
            addProperty("email", email);
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_FORGOT_PASSWORD)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            val jsonObject = Gson().fromJson(responseData, JsonObject::class.java)

            return jsonObject.get("message").asString
        }
    }

    fun updatePassword(email: String, password: String): String {
        val json = JsonObject().apply {
            addProperty("email", email);
            addProperty("newPassword", password)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_UPDATE_PASSWORD)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            val jsonObject = Gson().fromJson(responseData, JsonObject::class.java)

            return jsonObject.get("message").asString
        }
    }

    fun suggestRecipe(portions: String, category: String, time: String, complexity: String, nationality: String, ingredients: String, withoutIngredients: String, special: String, supriseMe: Boolean): JsonObject {
        val json = JsonObject().apply {
            addProperty("portions", portions);
            addProperty("category", category);
            addProperty("time", time);
            addProperty("complexity", complexity);
            addProperty("nationality", nationality);
            addProperty("ingredients", ingredients);
            addProperty("withoutIngredients", withoutIngredients);
            addProperty("specials", special);
            addProperty("supriseMe", supriseMe)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_RECIPE_SUGGESTIONS)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            return Gson().fromJson(responseData, JsonObject::class.java)
        }
    }

    companion object {
        const val BASE_URL="https://kitch-app-server.vercel.app"
        const val SERVER_ADDRESS_OPENAI = "$BASE_URL"
        const val SERVER_ADDRESS_LOGIN = "$BASE_URL/users/login"
        const val SERVER_ADDRESS_REGISTER = "$BASE_URL/users/register"
        const val SERVER_ADDRESS_DELETE_USER = "$BASE_URL/users/delete"
        const val SERVER_ADDRESS_FORGOT_PASSWORD = "$BASE_URL/users/findUser"
        const val SERVER_ADDRESS_UPDATE_PASSWORD = "$BASE_URL/users/updatePassword"
        const val SERVER_ADDRESS_RECIPE_SUGGESTIONS = "$BASE_URL/recipes"
    }
}