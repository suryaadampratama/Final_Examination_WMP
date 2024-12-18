package com.example.aplikasikrs.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aplikasikrs.databinding.FragmentNotificationsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        // Mengambil NIM dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val nim = sharedPreferences.getString("nim", null)

        if (nim != null) {
            loadUserData(nim)
        } else {
            showError("NIM tidak ditemukan")
        }

        // Tambahkan listener untuk tombol ubah data
        binding.updateButton.setOnClickListener {
            val intent = Intent(requireContext(), UpdateProfileActivity::class.java)
            intent.putExtra("nim", nim) // Kirim NIM ke activity baru
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Load data pengguna dari Firebase Realtime Database
    private fun loadUserData(nim: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(nim)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Mengambil data profil pengguna
                    val userName = snapshot.child("Nama").value.toString()
                    val email = snapshot.child("email").value.toString()
                    val jenisKelamin = snapshot.child("jenisKelamin").value.toString()
                    val tanggalLahir = snapshot.child("TanggalLahir").value.toString()
                    val tempatLahir = snapshot.child("TempatLahir").value.toString()
                    val nomorPonsel = snapshot.child("nomorPonsel").value.toString()

                    binding.profileName.text = userName
                    binding.profileNim.text = nim
                    binding.nilaiJenisKelamin.text = jenisKelamin
                    binding.tanggalLahir.text = tanggalLahir
                    binding.tempatLahir.text = tempatLahir
                    binding.nomorTeleponValue.text = nomorPonsel
                    binding.emailValue.text = email

                } else {
                    showError("Data profil tidak ditemukan untuk NIM $nim")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showError("Gagal memuat data: ${error.message}")
            }
        })
    }
    // Menampilkan pesan error
    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        binding.profileName.text = message
        binding.profileNim.text = ""
        binding.nilaiJenisKelamin.text = ""
        binding.tanggalLahir.text = ""
        binding.tempatLahir.text = ""
        binding.nomorTeleponValue.text = ""
        binding.emailValue.text = ""

    }
}
