package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

// Constant to hold the server address
const val SERVER_ADDRESS = "http://10.0.2.2:3000"
class MainActivity : AppCompatActivity() {

    // Instance of OkHTTPClient to handle HTTP requests
    private val client = OkHttpClient()

    // Function to send a request to the server and get the response as a String
    fun sendRequest(query: String): String {

        val json = JsonObject().apply {
            addProperty("query", query)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        // Create a new request to the specified server address
        val request = Request.Builder()
            .url(SERVER_ADDRESS)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


       // val textView: TextView = findViewById(R.id.text_response)

        val query = "Hi ChatGPT, whats up!"

        // Use a CoroutineScope to run the network request on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the sendRequest function to get the server response
                val response = sendRequest(query)
                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                   // textView.text = response  ?: "No response from server"
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                  //  textView.text = "Failed to get response"
                }
            }
        }
    }
    fun createRecipeButton(view: View){
        val intent = Intent(this, MainActivity_recipes::class.java)
        startActivity(intent)
    }

    fun freezerButton(view: View){
        val intent = Intent(this, MainActivity_Freezer::class.java)
        startActivity(intent)
    }

    fun fridgeButton(view: View){
        val intent = Intent(this, MainActivity_Fridge::class.java)
        startActivity(intent)
    }

    fun pantryButton(view: View){
        val intent = Intent(this, MainActivity_pantry::class.java)
        startActivity(intent)
    }


}