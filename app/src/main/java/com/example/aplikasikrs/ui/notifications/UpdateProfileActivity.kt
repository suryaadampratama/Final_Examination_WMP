package com.example.aplikasikrs.ui.notifications

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasikrs.databinding.ActivityUpdateProfileBinding
import com.google.firebase.database.FirebaseDatabase

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nim = intent.getStringExtra("nim") // Ambil NIM dari intent
        if (nim != null) {
            loadUserData(nim)
        }

        binding.buttonSave.setOnClickListener {
            val updatedData = mapOf(
                "jenisKelamin" to binding.editJenisKelamin.text.toString(),
                "TanggalLahir" to binding.editTanggalLahir.text.toString(),
                "TempatLahir" to binding.editTempatLahir.text.toString(),
                "nomorPonsel" to binding.editNomorTelepon.text.toString(),
                "email" to binding.editEmail.text.toString()
            )
            updateUserData(nim!!, updatedData)
        }
    }

    private fun loadUserData(nim: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(nim)

        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                binding.editJenisKelamin.setText(snapshot.child("jenisKelamin").value.toString())
                binding.editTanggalLahir.setText(snapshot.child("TanggalLahir").value.toString())
                binding.editTempatLahir.setText(snapshot.child("TempatLahir").value.toString())
                binding.editNomorTelepon.setText(snapshot.child("nomorPonsel").value.toString())
                binding.editEmail.setText(snapshot.child("email").value.toString())
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserData(nim: String, updatedData: Map<String, String>) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(nim)

        userRef.updateChildren(updatedData).addOnSuccessListener {
            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
        }
    }
}
