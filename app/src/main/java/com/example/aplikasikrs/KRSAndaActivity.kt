package com.example.aplikasikrs

import MataKuliahDiambilAdapter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasikrs.models.MataKuliahDiambil
import com.google.firebase.database.*

class KRSAndaActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var mataKuliahDiambilAdapter: MataKuliahDiambilAdapter
    private val mataKuliahDiambilList = mutableListOf<MataKuliahDiambil>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_krsanda)

        // Tombol kembali
        findViewById<TextView>(R.id.back_button).setOnClickListener { onBackPressed() }

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mataKuliahDiambilAdapter = MataKuliahDiambilAdapter(mataKuliahDiambilList)
        recyclerView.adapter = mataKuliahDiambilAdapter

        // Fetch user data
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val nim = sharedPreferences.getString("nim", null)

        if (nim == null) {
            Toast.makeText(this, "NIM tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        // Display user information
        displayUserInfo(nim)

        // Fetch data from Firebase
        fetchMataKuliahDiambil(nim)
    }

    private fun displayUserInfo(nim: String) {
        // Set NIM in TextView
        findViewById<TextView>(R.id.nim_value).text = nim

        // Referensi ke Firebase untuk detail user
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(nim)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Ambil data dari Firebase
                val nama = snapshot.child("Nama").getValue(String::class.java) ?: "Data tidak tersedia"
                val prodi = snapshot.child("prodi").getValue(String::class.java) ?: "Data tidak tersedia"
                val dosbing = snapshot.child("dosbing").getValue(String::class.java) ?: "Data tidak tersedia"

                // Set data ke TextView

                // Debugging: Log data
                Log.d("UserInfo", "Nama: $nama, Prodi: $prodi, Dosbing: $dosbing")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KRSAndaActivity, "Gagal memuat data pengguna: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchMataKuliahDiambil(nim: String) {
        database = FirebaseDatabase.getInstance().getReference("mataKuliahdiambil")
        database.orderByChild("nim").equalTo(nim).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mataKuliahDiambilList.clear()
                for (data in snapshot.children) {
                    val mataKuliahDiambil = data.getValue(MataKuliahDiambil::class.java)
                    if (mataKuliahDiambil != null) {
                        mataKuliahDiambilList.add(mataKuliahDiambil)
                    }
                }
                mataKuliahDiambilAdapter.notifyDataSetChanged()

                // Update total SKS
                val totalSKS = mataKuliahDiambilList.sumOf { it.sks }
                findViewById<TextView>(R.id.totalSKSTextView).text = totalSKS.toString()

                // Debugging: Log total SKS
                Log.d("KRSAnda", "Total SKS: $totalSKS")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KRSAndaActivity, "Gagal mengambil data KRS Anda: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
