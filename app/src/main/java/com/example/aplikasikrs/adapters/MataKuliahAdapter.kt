package com.example.aplikasikrs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasikrs.R
import com.example.aplikasikrs.models.MataKuliah

class MataKuliahAdapter(
    private val mataKuliahList: MutableList<MataKuliah>,
    private val onSelectionChanged: (MataKuliah, Boolean) -> Unit // Callback ketika status checkbox berubah
) : RecyclerView.Adapter<MataKuliahAdapter.ViewHolder>() {

    private val selectedItems = mutableSetOf<MataKuliah>() // Menyimpan mata kuliah yang dipilih

    fun updateData(newList: List<MataKuliah>) {
        mataKuliahList.clear()
        mataKuliahList.addAll(newList)
        selectedItems.clear() // Reset pilihan ketika data diperbarui
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matkul, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mataKuliah = mataKuliahList[position]
        holder.bind(mataKuliah, selectedItems.contains(mataKuliah)) { isSelected ->
            if (isSelected) {
                selectedItems.add(mataKuliah)
            } else {
                selectedItems.remove(mataKuliah)
            }
            onSelectionChanged(mataKuliah, isSelected)
        }
    }

    override fun getItemCount(): Int = mataKuliahList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mataKuliah: MataKuliah, isSelected: Boolean, onCheckedChange: (Boolean) -> Unit) {
            val checkBox = itemView.findViewById<CheckBox>(R.id.ivCheckbox)
            val textViewNama = itemView.findViewById<TextView>(R.id.tvNamaMatkul)
            val textViewSksValue = itemView.findViewById<TextView>(R.id.tvSksValue)
            val textViewKelasValue = itemView.findViewById<TextView>(R.id.tvKelasValue)

            textViewNama.text = mataKuliah.nama
            textViewSksValue.text = mataKuliah.sks.toString()
            textViewKelasValue.text = mataKuliah.kelas

            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = isSelected
            checkBox.setOnCheckedChangeListener { _, isChecked -> onCheckedChange(isChecked) }
        }
    }
}
