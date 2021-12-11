package com.example.brokegaming

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import android.widget.CompoundButton
import org.jetbrains.anko.find


class MainActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var signUp: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var switch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate called!")

        val preferences: SharedPreferences = getSharedPreferences("BrokeGaming", Context.MODE_PRIVATE)

        username= findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signUp = findViewById(R.id.signUp)
        progressBar = findViewById(R.id.progressBar)
        switch = findViewById(R.id.switch1)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        firebaseAnalytics.logEvent("login_screen_shown", null)

        login.isEnabled = false

        val restoreUsername = preferences.getString("RESTOREUSERNAME", "true")
        switch.isChecked = restoreUsername == "true"
        if(switch.isChecked){
            val savedUsername = preferences.getString("USERNAME", "")
            username.setText(savedUsername)
        }

        //The following code snippet is adapted from
        // https://newbedev.com/java-switchcompat-setoncheckedchangelistener-kotlin-code-example
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be true if the switch is in the On position
            if(buttonView.isChecked){
                preferences.edit().putString("RESTOREUSERNAME", "true").apply()
            }
            else{
                preferences.edit().putString("RESTOREUSERNAME", "false").apply()
            }
        }

        login.setOnClickListener{
            //Save the username to SharedPreferences
            val inputtedUsername = username.getText().toString()
            val inputtedPassword: String = password.text.toString()

            firebaseAuth.signInWithEmailAndPassword(inputtedUsername, inputtedPassword).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    firebaseAnalytics.logEvent("login_successful", null)

                    val currentUser: FirebaseUser = firebaseAuth.currentUser!!
                    Toast.makeText(this, "Logged in as: ${currentUser.email}", Toast.LENGTH_LONG).show()
                    val editor = preferences.edit()
                    editor.putString("USERNAME", inputtedUsername)
                    editor.apply()

                    progressBar.visibility = View.VISIBLE

                    val intent: Intent = Intent(this, GamesActivity::class.java)
//                    intent.putExtra("LOCATION", "Washington")
                    startActivity(intent)
                }
                else{
                    firebaseAnalytics.logEvent("login_failed", null)

                    val exception = task.exception
                    Toast.makeText(this, "Registration failed: $exception", Toast.LENGTH_LONG).show()
                }
            }

        }

        signUp.setOnClickListener {
            val intent: Intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
            Log.d("MainActivity", "Text is ${username.getText().toString()}")
            val inputtedUsername: String = username.text.toString()
            val inputtedPassword: String = password.text.toString()
            val enableButton: Boolean = inputtedUsername.isNotBlank() && inputtedPassword.isNotBlank()
            login.isEnabled = enableButton
        }

        override fun afterTextChanged(p0: Editable?){}
    }
}