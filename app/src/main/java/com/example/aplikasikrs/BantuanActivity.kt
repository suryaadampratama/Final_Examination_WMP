package com.example.aplikasi

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasikrs.R
import java.util.Stack

class BantuanActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnChangeImage: Button
    private lateinit var btnResetImage: Button

    // Daftar gambar yang akan digunakan
    private val imageList = listOf(
        R.drawable.image_bantuan1,
        R.drawable.image_bantuan2,
        R.drawable.image_bantuan3,
        R.drawable.image_bantuan4,
        R.drawable.image_bantuan5
    )

    // Stack untuk menyimpan gambar sebelumnya
    private val previousImagesStack = Stack<Int>()

    // Indeks untuk melacak gambar saat ini
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)

        // Inisialisasi komponen
        imageView = findViewById(R.id.imageView)
        btnChangeImage = findViewById(R.id.btnChangeImage)
        btnResetImage = findViewById(R.id.btnResetImage)

        // Tampilkan gambar pertama secara default
        imageView.setImageResource(imageList[currentIndex])

        // Atur aksi tombol "Change Image"
        btnChangeImage.setOnClickListener {
            changeImage()
        }

        // Atur aksi tombol "Reset Image"
        btnResetImage.setOnClickListener {
            resetImage()
        }
    }

    // Fungsi untuk mengganti gambar ke gambar berikutnya
    private fun changeImage() {
        // Simpan gambar saat ini ke dalam stack
        previousImagesStack.push(imageList[currentIndex])

        // Pindah ke gambar berikutnya
        currentIndex = (currentIndex + 1) % imageList.size

        // Tampilkan gambar baru
        imageView.setImageResource(imageList[currentIndex])
    }

    // Fungsi untuk mereset gambar ke gambar sebelumnya
    private fun resetImage() {
        if (previousImagesStack.isNotEmpty()) {
            // Ambil gambar terakhir dari stack
            val previousImage = previousImagesStack.pop()

            // Cari indeks gambar sebelumnya di imageList
            currentIndex = imageList.indexOf(previousImage)

            // Tampilkan gambar sebelumnya
            imageView.setImageResource(previousImage)
        }
    }
}
