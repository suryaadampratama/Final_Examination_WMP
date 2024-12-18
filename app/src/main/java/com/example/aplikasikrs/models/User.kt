package com.example.aplikasikrs

data class User(
    val userId: String,
    val nim: String,
    val email: String,
    val nomorPonsel: String,
    val password: String,
    val mataKuliahYangDipilih: Map<String, MataKuliah> = emptyMap() // Default kosong
)

data class MataKuliah(
    val nama: String = "",
    val sks: Int = 0,
    val kelas: String = ""
)