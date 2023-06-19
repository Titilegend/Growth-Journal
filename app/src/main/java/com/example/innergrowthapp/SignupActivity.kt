package com.example.innergrowthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.innergrowthapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore
    //val usersCollection = firestore.collection("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)
        signupBinding.buttonSignUp.setOnClickListener {
            val email = signupBinding.emailSignup.text.toString()
            val password = signupBinding.passwordSignup.text.toString()
            signinUser(email, password)
        }
        signupBinding.textViewLogin.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun signinUser(email: String, userPassword: String) {
        signupBinding.progressBar.visibility = View.VISIBLE
        signupBinding.buttonSignUp.isClickable = false


        auth.createUserWithEmailAndPassword(email, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userId = it.uid
                    // Save user details to Firestore
                    saveUserDetailsToFirestore(userId, email, userPassword)
                    Toast.makeText(
                        applicationContext,
                        "Your account has been created",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()

                    signupBinding.progressBar.visibility = View.INVISIBLE
                    signupBinding.buttonSignUp.isClickable = true


                }
            }; else {
                Toast.makeText(
                    applicationContext,
                    task.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }
        fun saveUserDetailsToFirestore(userId: String, email: String, userPassword: String) {
            val user = HashMap<String, Any>()
            user["email"] = email
            user["password"] = userPassword

            firestore.collection("users").document(userId).set(user)
                .addOnSuccessListener {documentReference ->
                    // User details saved successfully
                    Log.d("Firestore", "User details saved successfully")
                }
                .addOnFailureListener { e ->
                    // Error saving user details
                    Log.e("FirestoreError", "Error saving user details: $e")
                }
        }

}
