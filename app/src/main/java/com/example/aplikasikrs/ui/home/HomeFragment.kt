package com.example.aplikasikrs.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasi.BantuanActivity
import com.example.aplikasikrs.KRSActivity
import com.example.aplikasikrs.KRSAndaActivity
import com.example.aplikasikrs.R
import com.example.aplikasikrs.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Tambahkan logika untuk tombol ambil KRS
        val ambilKrsButton: Button = binding.root.findViewById(R.id.ambil_krs_button)
        ambilKrsButton.setOnClickListener {
            // Intent untuk membuka KRSActivity
            val intent = Intent(requireContext(), KRSActivity::class.java)
            startActivity(intent)
        }
        val krsAndaButton: Button = binding.root.findViewById(R.id.krs_anda_button)
        krsAndaButton.setOnClickListener {
            // Intent untuk membuka KRSAndaActivity
            val intent = Intent(requireContext(), KRSAndaActivity::class.java)
            startActivity(intent)
        }
        val bantuanButton: Button = binding.root.findViewById(R.id.button_bantuan)
        bantuanButton.setOnClickListener {
            // Intent untuk membuka KRSActivity
            val intent = Intent(requireContext(), BantuanActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}