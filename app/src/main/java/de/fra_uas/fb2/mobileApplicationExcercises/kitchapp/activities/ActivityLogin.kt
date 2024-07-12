package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.R
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.fragments.LoadingDialogFragment
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.NetworkHelper
import android.util.Log
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.ValidationUtil
import org.json.JSONObject
import java.io.IOException
//BEGIN_Raj
class ActivityLogin : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText

    private  val networkHelper = NetworkHelper()
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)

        loadingDialog = LoadingDialogFragment()
        sessionManager = SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToMainActivity()
        }
    }

    fun signUpButton(view: View){
        val intent = Intent(this, ActivityCreateAccount::class.java)
        startActivity(intent)
    }

    fun loginButton(view: View){
        // Validation of Inputs using the Validation Class
        val validationError = ValidationUtil.validateLoginInputs(email.text.toString(), password.text.toString())

        if (validationError != null) {
            Toast.makeText(applicationContext, validationError, Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, ActivityHome::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    loadingDialog.show(supportFragmentManager, "loadingDialog")
                }
                val response = networkHelper.login(email.text.toString(), password.text.toString())
                // Save JSON response from User
                val userData = JSONObject(response.toString())
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Log.d("Data from Login: ", response.toString())
                    sessionManager.createLoginSession(userData)
                    startActivity(intent)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Log.d("SERVER ERROR", "Login Failed - ${e}")
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent: Intent = Intent(this, ActivityHome::class.java)
        startActivity(intent)
        finish()
    }

    fun forgotPasswordButton(view: View){
        val intent: Intent = Intent(this, ActivityForgotPassword::class.java)
        startActivity(intent)
        finish()
    }
}
//END_Raj