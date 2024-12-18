package com.example.aplikasikrs.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasikrs.R
import com.example.aplikasikrs.models.Dosen

class DosenAdapter(private val dosenList: List<Dosen>, private val context: Context) : RecyclerView.Adapter<DosenAdapter.DosenViewHolder>() {

    inner class DosenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaDosen: TextView = itemView.findViewById(R.id.nama_dosen)
        private val prodiDosen: TextView = itemView.findViewById(R.id.prodi_dosen)
        private val chatIcon: ImageView = itemView.findViewById(R.id.chat_icon)

        fun bind(dosen: Dosen) {
            namaDosen.text = dosen.nama
            prodiDosen.text = dosen.prodi

            // Set OnClickListener untuk ikon chat
            chatIcon.setOnClickListener {
                dosen.whatsapp?.let { nomor ->
                    openWhatsApp(nomor)
                }
            }
        }

        private fun openWhatsApp(phoneNumber: String) {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DosenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dosen, parent, false)
        return DosenViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dosenList.size
    }

    override fun onBindViewHolder(holder: DosenViewHolder, position: Int) {
        holder.bind(dosenList[position])
    }
}