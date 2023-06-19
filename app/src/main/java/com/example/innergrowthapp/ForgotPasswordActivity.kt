package com.example.innergrowthapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.innergrowthapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var forgetBinding:ActivityForgotPasswordBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgetBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgetBinding.root
        setContentView(view)
        forgetBinding.buttonContinue.setOnClickListener {
            val resetEmail = forgetBinding.emailReset.text.toString()
            auth.sendPasswordResetEmail(resetEmail).addOnCompleteListener { task->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"we sent a reset email to you", Toast.LENGTH_LONG).show()
                    finish()

                }else{
                    Toast.makeText(applicationContext,"we sent a reset email to you", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}