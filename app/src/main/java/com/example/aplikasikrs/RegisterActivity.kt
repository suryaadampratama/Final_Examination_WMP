package com.example.aplikasikrs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    // Referensi Firebase Realtime Database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Pastikan nama file layout adalah activity_register.xml

        // Inisialisasi elemen UI
        val inputNIM = findViewById<EditText>(R.id.input_nim)
        val inputEmail = findViewById<EditText>(R.id.input_email)
        val inputNomorPonsel = findViewById<EditText>(R.id.input_nomor_ponsel)
        val inputBuatPassword = findViewById<EditText>(R.id.input_buat_password)
        val inputKonfirmasiPassword = findViewById<EditText>(R.id.input_konfirmasi_password)
        val radioSetuju = findViewById<RadioButton>(R.id.radio_setuju)
        val btnSelanjutnya = findViewById<Button>(R.id.btn_selanjutnya)
        val textSudahMemilikiAkun = findViewById<TextView>(R.id.text_sudah_memiliki_akun)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("users")

        // Aksi tombol "SELANJUTNYA"
        btnSelanjutnya.setOnClickListener {
            val nim = inputNIM.text.toString().trim()
            val email = inputEmail.text.toString().trim()
            val nomorPonsel = inputNomorPonsel.text.toString().trim()
            val buatPassword = inputBuatPassword.text.toString()
            val konfirmasiPassword = inputKonfirmasiPassword.text.toString()

            // Validasi input
            if (nim.isEmpty() || email.isEmpty() || nomorPonsel.isEmpty() || buatPassword.isEmpty() || konfirmasiPassword.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (buatPassword != konfirmasiPassword) {
                Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!radioSetuju.isChecked) {
                Toast.makeText(this, "Anda harus menyetujui syarat dan ketentuan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan data ke Firebase
            val userId = database.push().key ?: ""
            val user = User(userId, nim, email, nomorPonsel, buatPassword)

            database.child(nim).setValue(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    // Navigasi ke aktivitas login
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Aksi teks "Sudah memiliki akun?"
        textSudahMemilikiAkun.setOnClickListener {
            // Navigasi ke halaman login
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
