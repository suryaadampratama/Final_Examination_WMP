package com.example.aplikasikrs.ui.message

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasikrs.KonsulActivity
import com.example.aplikasikrs.R
import com.example.aplikasikrs.databinding.FragmentMessageBinding

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Menggunakan ViewModel yang sesuai untuk MessageFragment
        val messageViewModel =
            ViewModelProvider(this).get(MessageViewModel::class.java)

        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Menangani klik pada tombol
        val konsultasiOnlineButton: Button = binding.konsultasionline // Mengambil referensi tombol
        konsultasiOnlineButton.setOnClickListener {
            // Navigasi ke KonsulActivity
            val intent = Intent(requireContext(), KonsulActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}