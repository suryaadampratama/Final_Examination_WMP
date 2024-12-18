import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasikrs.R
import com.example.aplikasikrs.models.MataKuliahDiambil

class MataKuliahDiambilAdapter(private val mataKuliahDiambilList: List<MataKuliahDiambil>) :
    RecyclerView.Adapter<MataKuliahDiambilAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaMatkul: TextView = itemView.findViewById(R.id.tvNamaMatkul)
        val sksValue: TextView = itemView.findViewById(R.id.tvSksValue)
        val kelasValue: TextView = itemView.findViewById(R.id.tvKelasValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_matkulpilih, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mataKuliahDiambil = mataKuliahDiambilList[position]
        holder.namaMatkul.text = mataKuliahDiambil.nama
        holder.sksValue.text = mataKuliahDiambil.sks.toString()
        holder.kelasValue.text = mataKuliahDiambil.kelas // assuming `kelas` is a field in the model
    }

    override fun getItemCount(): Int {
        return mataKuliahDiambilList.size
    }
}
