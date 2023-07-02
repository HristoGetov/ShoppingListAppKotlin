package com.example.shoppinglistaappkotlin.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppinglistaappkotlin.R
import com.example.shoppinglistaappkotlin.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                          goToMain()
                        }else{
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }else{
                Toast.makeText(this,"Empty fields are not allowed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToMain(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser !=null){
            goToMain()
        }
    }
}