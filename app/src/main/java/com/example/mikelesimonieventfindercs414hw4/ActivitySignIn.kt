package com.example.mikelesimonieventfindercs414hw4



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class ActivitySignIn : AppCompatActivity() {
    private val RC_SIGN_IN = 123 // Request code for sign in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Check if user is signed in, if yes, navigate to main activity
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            startActivity(Intent(this, MainActivity::class.java))
            finish() // finish current activity
        } else {
            // User is signed out
            findViewById<Button>(R.id.signInButton).setOnClickListener {
                createSignInIntent()
            }
        }
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder()
                 // Request ID token for Firebase Authentication
                .build(),
//            AuthUI.IdpConfig.FacebookBuilder().build()
            // Add more providers if needed
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                startActivity(Intent(this, MainActivity::class.java))
                finish() // finish current activity
            } else {
                // Sign in failed
                // Handle sign-in failure, e.g., display an error message
            }
        }
    }
}
