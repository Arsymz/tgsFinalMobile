package com.example.recyclerview.view.daftar

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.data.prefer.UserModel
import com.example.recyclerview.databinding.ActivityDaftarBinding
import com.example.recyclerview.di.Injection
import com.example.recyclerview.view.login.Login
import kotlinx.coroutines.runBlocking

class Daftar : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }
    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun setupAction(){
        binding.btnDaftar.setOnClickListener {
            val email=binding.edtEmail.text.toString()
            val password=binding.edtPass.text.toString()

            val userPreference=Injection.provideRepository(this)
            val userModel=UserModel(email, password,true)
            runBlocking { userPreference.saveSession(userModel) }

            val intent=Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}