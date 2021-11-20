package com.example.brokegaming

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*

class SignUpActivity: AppCompatActivity() {
    private lateinit var signUpEmail: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var signUp: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        Log.d("SignUpActivity", "onCreate called!")
        //Get data from the Intent that launched this screen
        val intent: Intent = getIntent()

        signUpEmail = findViewById(R.id.signUp_email)
        signUpPassword = findViewById(R.id.signUp_password)
        confirmPassword = findViewById(R.id.confirm_password)
        signUp = findViewById(R.id.signUp_button)
        progressBar = findViewById(R.id.progressBar2)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        firebaseAnalytics.logEvent("sign_up_screen_shown", null)

        val inputtedEmail: String = signUpEmail.text.toString()
        val inputtedPassword: String = signUpPassword.text.toString()
        val samePassword: String = confirmPassword.text.toString()

        signUp.setOnClickListener {
            if(inputtedPassword.equals(samePassword)){
                firebaseAuth.createUserWithEmailAndPassword(inputtedEmail, inputtedPassword)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    val currentUser: FirebaseUser = firebaseAuth.currentUser!!
                                    Toast.makeText(this, "Registered successfully: ${currentUser.email}", Toast.LENGTH_LONG).show()
                                }
                                else{
                                    val exception = task.exception
                                    if(exception is FirebaseAuthInvalidCredentialsException){
                                        Toast.makeText(this, "Registration failed: you did not supply a valid email address or password", Toast.LENGTH_LONG).show()
                                    }
                                    else if(exception is FirebaseAuthUserCollisionException){
                                        Toast.makeText(this, "Registration failed: this account already exists!", Toast.LENGTH_LONG).show()
                                    }
                                    else if(exception is FirebaseAuthWeakPasswordException){
                                        Toast.makeText(this, "Registration failed: your password does not meet minimum requirements!", Toast.LENGTH_LONG).show()
                                    }
                                    else{
                                        Toast.makeText(this, "Registration failed: $exception", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Registration failed: Please confirm your password.", Toast.LENGTH_LONG).show()
            }
        }
    }
}