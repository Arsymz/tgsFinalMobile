package com.example.recyclerview.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.example.recyclerview.ViewModelFactory
import com.example.recyclerview.data.UsersRepository
import com.example.recyclerview.data.prefer.UserPreference
import com.example.recyclerview.data.prefer.dataStore
import com.example.recyclerview.databinding.ActivityLoginBinding
import com.example.recyclerview.view.daftar.Daftar
import com.example.recyclerview.view.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    private val viewModel by viewModels<LoginModeView> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var usersRepository: UsersRepository
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usersRepository = UsersRepository.getInstance(UserPreference.getInstance(this.dataStore))
        setupView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnDaftar.setOnClickListener {
            startActivity(Intent(this, Daftar::class.java))
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password wajib diisi", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.viewModelScope.launch {
                    val userModel = usersRepository.getSession().first()
                    if (email == userModel.email && password == userModel.password) {
                        startActivity(
                            Intent(
                                this@Login, MainActivity::class.java
                            )
                        )
                    } else {
                        Toast.makeText(this@Login, "email dan password salah", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
    }

    private fun setupView() {
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


}