package com.example.shoppinglistaappkotlin.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.shoppinglistaappkotlin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var userId: String

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

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
                                userId = firebaseAuth.currentUser?.uid.toString()
                                val docReference = fireStore.collection("users").document(userId)
                                val users = HashMap<String, Any>()
                                users["firstName"] = binding.firstName.text.toString()
                                users["lastName"] = binding.lastName.text.toString()
                                users["email"] = binding.emailInput.text.toString()
                                docReference.set(users).addOnSuccessListener {
                                    Toast.makeText(this,"User successfully added!",Toast.LENGTH_SHORT).show()
                                }
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
