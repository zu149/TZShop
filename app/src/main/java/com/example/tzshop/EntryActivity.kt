package com.example.tzshop

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.example.tzshop.data.users
import com.example.tzshop.databinding.ActivityEntryBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.concurrent.TimeUnit


class EntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryBinding

    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        if (FirebaseAuth.getInstance().currentUser != null) { //автоматический вход залогиненного пользователя
            val intent = Intent(this@EntryActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) { // toast

            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG", "onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token

            }

        }


        firestore = FirebaseFirestore.getInstance()

        binding.button2.setOnClickListener {
            if (TextUtils.isEmpty(binding.sms.text.toString())) {
                Toast.makeText(
                    this, "Bведите код", Toast.LENGTH_SHORT
                ).show()
            }else {
                val x : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId.toString(), binding.sms.text.toString())
                signInWithPhoneAuthCredential(x)

            }


        }

        binding.button.setOnClickListener {

            val phone = binding.phone.text.toString()
            sendCode(phone)
            binding.sms.visibility = View.VISIBLE
            Toast.makeText(
                this, "заполните код подтверждения высланный на телефон", Toast.LENGTH_SHORT
            ).show()

            binding.button.visibility = View.GONE
            binding.button2.visibility = View.VISIBLE

//            val intent = Intent(this@EntryActivity, MainActivity::class.java)
//            startActivity(intent)
        }

        binding.button.isEnabled = false
        binding.button2.isEnabled = false
        binding.sms.visibility= View.GONE //скрытие кода

        var checkName = false
        var checkSurname = false
        var checkPhone = false
        var checkSms = false

        binding.name.doAfterTextChanged {
            val input = it.toString()
            if (input.matches("[а-яА-Яa-zA-Z]+".toRegex())) {
                binding.name.setBackgroundColor(Color.TRANSPARENT)
                checkName = true
                if (checkName && checkSurname && checkPhone) {
                    binding.button.isEnabled = true
                    binding.button.setBackgroundColor(0xFFEC407A.toInt())
                }
            } else {
                binding.name.setBackgroundResource(R.drawable.validate)
                binding.name.setError("error")
                checkName = false
            }
        }

        binding.surname.doAfterTextChanged {
            val input = it.toString()
            if (input.matches("[а-яА-Яa-zA-Z]+".toRegex())) {
                binding.surname.setBackgroundColor(Color.TRANSPARENT)
                checkSurname = true
                if (checkName && checkSurname && checkPhone) {
                    binding.button.isEnabled = true
                    binding.button.setBackgroundColor(0xFFEC407A.toInt())
                }
            } else {
                binding.surname.setBackgroundResource(R.drawable.validate)
                binding.surname.setError("error")
                checkSurname = false
            }
        }

        binding.phone.doAfterTextChanged {//! меняет на противоложное значение
            if (!TextUtils.isEmpty(binding.phone.text.toString())) {
                checkPhone = true
                if (checkName && checkSurname && checkPhone) {
                    binding.button.isEnabled = true
                    binding.button.setBackgroundColor(0xFFEC407A.toInt())
                }
            }else {
                checkPhone = false
            }
        }

        binding.sms.doAfterTextChanged {//! меняет на противоложное значение
            if (!TextUtils.isEmpty(binding.sms.text.toString())) {
                checkSms = true
                if (checkSms) {
                    binding.button2.isEnabled = true
                    binding.button2.setBackgroundColor(0xFFEC407A.toInt())
                }
            }else {
                checkSms = false
            }
        }

// Поле телефона
        val phoneNumberEditText = findViewById<EditText>(R.id.phone)

        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString()
                    val formattedInput = formatPhoneNumber(input)
                    if (formattedInput != input) {
                        phoneNumberEditText.setText(formattedInput)
                        phoneNumberEditText.setSelection(formattedInput.length)
                    }
                }
            }
        })



    }

    private fun saveUserToFirestore(user: users) {
        firestore.collection("users").document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {

            }

    }

    // валидация телефона
    private fun formatPhoneNumber(input: String): String {
        val digits = input.filter { it.isDigit() }

        // Ignore leading '7'
        val formattedDigits = if (digits.startsWith("7")) {
            "+7" + digits.substring(1)
        } else {
            "+7$digits"
        }

        val formattedPhoneNumber = StringBuilder()

        for (i in formattedDigits.indices) {
            when (i) {
                2, 5 -> formattedPhoneNumber.append(" ")
                8, 10 -> formattedPhoneNumber.append(" ")
            }
            formattedPhoneNumber.append(formattedDigits[i])
        }

        return formattedPhoneNumber.toString()
    }


    fun sendCode(phone : String) { // отправка смс
        val phoneOptions = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phone).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBack).build()
        PhoneAuthProvider.verifyPhoneNumber(phoneOptions)
    }

    private fun signInWithPhoneAuthCredential(x: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(x).addOnCompleteListener(this) {
            if (it.isSuccessful) {

                val name = binding.name.text.toString().trim()
                val surname = binding.surname.text.toString().trim()
                val phone = binding.phone.text.toString().trim()


                if (name.isNotEmpty() && surname.isNotEmpty() && phone.isNotEmpty()) {
                    val id = it.result.user!!.uid
                    val user = users(name = name, surname = surname, phone = phone, id = id)
                    saveUserToFirestore(user)
                } else {

                }

                val intent = Intent(this@EntryActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }



}