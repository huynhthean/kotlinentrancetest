package com.nexlesoft.ket.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.nexlesoft.ket.R
import com.nexlesoft.ket.data.api.Resource
import com.nexlesoft.ket.data.model.SignUpResponse
import com.nexlesoft.ket.databinding.ActivityMainBinding
import com.nexlesoft.ket.ui.category.CategoryListActivity
import com.nexlesoft.ket.utils.setWindowFullScreen
import com.nexlesoft.ket.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowFullScreen()
        bindUI()
    }

    private fun bindUI() {
        binding.btnNext.setOnClickListener {
            if (emailValidate() && passwordValidate()) {
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.getPasswordText()
                viewModel.createNewUser(email, password)
            }
        }

        viewModel.signUpResponse.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    val email = binding.edtEmail.text.toString()
                    val password = binding.edtPassword.getPasswordText()
                    viewModel.loginWithEmailAndPassword(email, password)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    toast(result.message.toString())
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
        viewModel.signInResponse.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    startActivity(Intent(this@MainActivity, CategoryListActivity::class.java))
                }
                is Resource.Error -> {
                    hideProgressBar()
                    toast(result.message.toString())
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }


    private fun passwordValidate(): Boolean {
        if (binding.edtPassword.isEmpty()) {
            toast(getString(R.string.password_required_message))
            return false
        }
        if (!binding.edtPassword.isValidPassword()) {
            toast(getString(R.string.password_length_message))
            return false
        }
        return true
    }

    private fun emailValidate(): Boolean {
        val emailAddress = binding.edtEmail.text.toString()
        if (emailAddress.isEmpty()) {
            toast(getString(R.string.email_required_message))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            toast(getString(R.string.email_invalid_message))
            return false
        }
        return true
    }

    private fun hideProgressBar() {
        binding.pbLoading.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.pbLoading.visibility = View.VISIBLE
    }
}