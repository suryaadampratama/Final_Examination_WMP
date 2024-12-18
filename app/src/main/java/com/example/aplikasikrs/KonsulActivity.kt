package com.example.aplikasikrs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasikrs.adapters.DosenAdapter
import com.example.aplikasikrs.models.Dosen
import com.google.firebase.database.*

class KonsulActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dosenAdapter: DosenAdapter
    private lateinit var dosenList: MutableList<Dosen>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konsul) // Ganti dengan layout Anda

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        dosenList = mutableListOf()
        dosenAdapter = DosenAdapter(dosenList, this) // Kirim konteks
        recyclerView.adapter = dosenAdapter

        database = FirebaseDatabase.getInstance().getReference("dosen_pembimbing")

        // Mengambil data dari Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dosenList.clear()
                for (dosenSnapshot in snapshot.children) {
                    val dosen = dosenSnapshot.getValue(Dosen::class.java)
                    dosen?.let { dosenList.add(it) }
                }
                dosenAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}