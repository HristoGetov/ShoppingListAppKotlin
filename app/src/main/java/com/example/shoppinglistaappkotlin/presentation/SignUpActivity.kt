package com.example.shoppinglistaappkotlin.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppinglistaappkotlin.R
import com.example.shoppinglistaappkotlin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth:FirebaseAuth

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

            binding.textView.setOnClickListener{
               goToSignInActivity()
            }
            binding.signUpBtn.setOnClickListener {
                val email = binding.emailInput.text.toString()
                val password = binding.password.text.toString()
                val confirmPassword = binding.confirmPassword.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                    if (password == confirmPassword){
                        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                            if (it.isSuccessful){
                                goToSignInActivity()
                            }else{
                                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        Toast.makeText(this,"Password is not matching!",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this,"Empty fields are not allowed!",Toast.LENGTH_LONG).show()
                }

        }

    }
    private fun goToSignInActivity(){
        val intent = Intent(this,SignInActivity::class.java)
        startActivity(intent)
    }
}
