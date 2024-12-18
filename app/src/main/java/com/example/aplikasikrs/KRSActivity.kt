package com.example.aplikasikrs

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasikrs.adapters.MataKuliahAdapter
import com.example.aplikasikrs.models.MataKuliah
import com.google.firebase.database.*

class KRSActivity : AppCompatActivity() {
    private var totalSKS: Int = 0
    private lateinit var database: DatabaseReference
    private lateinit var mataKuliahAdapter: MataKuliahAdapter
    private val selectedMataKuliah = mutableListOf<MataKuliah>() // Menyimpan mata kuliah yang dipilih

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_krsactivity)

        // Tombol kembali
        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener { onBackPressed() }

        // Inisialisasi RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val mataKuliahList = mutableListOf<MataKuliah>()

        // Adapter untuk RecyclerView
        mataKuliahAdapter = MataKuliahAdapter(mataKuliahList) { mataKuliah, isSelected ->
            updateSelectedMataKuliah(mataKuliah, isSelected)
        }
        recyclerView.adapter = mataKuliahAdapter

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance().getReference("mataKulia")

        // Ambil data dari Firebase
        fetchMataKuliahData()

        // Tombol Simpan
        val simpanButton: Button = findViewById(R.id.simpan_button)
        simpanButton.setOnClickListener { saveSelectedMataKuliah() }
    }

    private fun fetchMataKuliahData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mataKuliahList = mutableListOf<MataKuliah>()
                for (data in snapshot.children) {
                    val mataKuliah = data.getValue(MataKuliah::class.java)
                    if (mataKuliah != null) {
                        mataKuliahList.add(mataKuliah)
                    }
                }
                mataKuliahAdapter.updateData(mataKuliahList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KRSActivity, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSelectedMataKuliah(mataKuliah: MataKuliah, isSelected: Boolean) {
        if (isSelected) {
            selectedMataKuliah.add(mataKuliah)
        } else {
            selectedMataKuliah.remove(mataKuliah)
        }
        totalSKS = selectedMataKuliah.sumOf { it.sks }
        updateTotalSKS(totalSKS)
    }

    private fun updateTotalSKS(total: Int) {
        totalSKS = total.coerceAtLeast(0)
        val totalSKSTextView: TextView = findViewById(R.id.totalSKSTextView)
        totalSKSTextView.text = "SKS yang diambil: $totalSKS"
    }

    private fun saveSelectedMataKuliah() {
        val sharedPreferences = getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
        val nim = sharedPreferences.getString("nim", null)

        if (nim == null) {
            Toast.makeText(this, "NIM tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        clearMataKuliahDiambil(nim, onSuccess = {
            val mataKuliahRef = FirebaseDatabase.getInstance().getReference("mataKuliahdiambil")
            selectedMataKuliah.forEach { mataKuliah ->
                val key = mataKuliahRef.push().key
                if (key != null) {
                    mataKuliahRef.child(key).setValue(
                        mapOf(
                            "nim" to nim,
                            "kelas" to mataKuliah.kelas,
                            "nama" to mataKuliah.nama,
                            "sks" to mataKuliah.sks
                        )
                    )
                }
            }
            Toast.makeText(this, "Data mata kuliah berhasil disimpan!", Toast.LENGTH_SHORT).show()
        }, onFailure = { errorMessage ->
            Toast.makeText(this, "Gagal menghapus data lama: $errorMessage", Toast.LENGTH_SHORT).show()
        })
    }

    private fun clearMataKuliahDiambil(nim: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val mataKuliahRef = FirebaseDatabase.getInstance().getReference("mataKuliahdiambil")

        mataKuliahRef.orderByChild("nim").equalTo(nim).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        child.ref.removeValue()
                    }
                }
                onSuccess()
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error.message)
            }
        })
    }
}
