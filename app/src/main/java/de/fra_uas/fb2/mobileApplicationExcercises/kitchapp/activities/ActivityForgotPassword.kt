package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ActivityForgotPassword : AppCompatActivity() {
    // BEGIN: Raj
    private lateinit var editTextEmail: EditText
    private lateinit var loadingDialog: LoadingDialogFragment
    private  val networkHelper = NetworkHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgotpassword)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextEmail = findViewById(R.id.editTextEmail)
        loadingDialog = LoadingDialogFragment()

    }

    fun forgotPassword(view: View) {
        val validationError = ValidationUtil.isValidEmail(editTextEmail.text.toString())

        if (!validationError) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, ActivityResetPassword::class.java).apply {
            putExtra("email", editTextEmail.text.toString())
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    loadingDialog.show(supportFragmentManager, "loadingDialog")
                }
                val response = networkHelper.forgotPassword(editTextEmail.text.toString())
                withContext(Dispatchers.Main) {
                    loadingDialog.dismiss()
                    Toast.makeText(applicationContext, "Email verified", Toast.LENGTH_SHORT).show()
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
}
// END: Raj