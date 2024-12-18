package com.example.aplikasikrs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var inputNim: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnregister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi View
        inputNim = findViewById(R.id.input_nim)
        inputPassword = findViewById(R.id.input_password)
        btnLogin = findViewById(R.id.btn_selanjutnya)

        btnregister = findViewById(R.id.text_belum_memiliki_akun)

        btnregister.setOnClickListener {
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // SharedPreferences untuk menyimpan data login
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Tombol Login
        btnLogin.setOnClickListener {
            val nim = inputNim.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            if (validateInput(nim, password)) {
                loginUser(nim, password)
            }
        }
    }

    private fun validateInput(nim: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(nim) -> {
                inputNim.error = "NIM tidak boleh kosong"
                false
            }
            TextUtils.isEmpty(password) -> {
                inputPassword.error = "Password tidak boleh kosong"
                false
            }
            else -> true
        }
    }

    private fun loginUser(nim: String, password: String) {
        // Referensi Firebase
        val database = FirebaseDatabase.getInstance().getReference("users")

        database.child(nim).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userPassword = snapshot.child("password").getValue(String::class.java)

                    if (userPassword == password) {
                        // Simpan ke SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("nim", nim)
                        editor.putString("nama", snapshot.child("Nama").getValue(String::class.java))
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        Toast.makeText(this@MainActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                        // Navigasi ke halaman utama
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Password salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "NIM tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
