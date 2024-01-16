package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()



        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.registerButton.setOnClickListener{
            val email = binding.Email.text.toString()
            val username = binding.userName.text.toString()
            val password = binding.password.text.toString()
            val repeatpassword = binding.repassword.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || repeatpassword.isEmpty())
            {
                Toast.makeText(this, "Please fill all Details", Toast.LENGTH_SHORT).show()
            }
            else if(password != repeatpassword)
            {
                Toast.makeText(this, "Both passwords are different", Toast.LENGTH_SHORT).show()
            }
            else

            {
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                        }
                    }





            }
        }

    }
}

