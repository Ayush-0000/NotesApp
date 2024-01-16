package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val userName = binding.userName.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (userName.isEmpty() || password.isEmpty())
                Toast.makeText(this, "Fill in the details", Toast.LENGTH_SHORT).show()
            else {
                auth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Sign In failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}


//package com.example.notesapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.notesapp.databinding.ActivityLoginBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//
//class LoginActivity : AppCompatActivity() {
//    private val binding: ActivityLoginBinding by lazy {
//        ActivityLoginBinding.inflate(layoutInflater)
//    }
//    private lateinit var auth: FirebaseAuth
//
//    override fun onStart() {
//        super.onStart()
//        val currentUser: FirebaseUser? = auth.currentUser
//        if (currentUser != null) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        auth = FirebaseAuth.getInstance()
//
//        binding.loginButton.setOnClickListener {
//            val userName = binding.userName.text.toString().trim()
//            val password = binding.password.text.toString().trim()
//
//            if (userName.isEmpty() || password.isEmpty())
//                Toast.makeText(this, "Fill in the details", Toast.LENGTH_SHORT).show()
//            else {
//                auth.signInWithEmailAndPassword(userName, password)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()
//                            startActivity(Intent(this, MainActivity::class.java))
//                            finish()
//                        } else {
//                            Toast.makeText(this, "Sign In failed", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            }
//        }
//
//        binding.signUpButton.setOnClickListener {
//            startActivity(Intent(this, SignUpActivity::class.java))
//            finish()
//        }
//    }
//}
