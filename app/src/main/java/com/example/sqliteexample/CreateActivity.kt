package com.example.sqliteexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteexample.databinding.ActivityCreateBinding

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var studentDBHelper: StudentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menyiapkan toolbar dengan tombol kembali
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inisialisasi helper database
        studentDBHelper = StudentDBHelper(this)

        // Menangani event klik tombol Simpan
        binding.btnSimpan.setOnClickListener {
            val nim = binding.etNIM.text.toString()
            val name = binding.etNama.text.toString()
            val age = binding.etUmur.text.toString()

            // Validasi input
            if (nim.isEmpty() || name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Silahkan masukkan data NIM, nama, dan umur!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Membuat objek mahasiswa baru
            val newStudent = StudentModel(nim = nim, name = name, age = age)

            // Menyimpan data mahasiswa ke database
            val result = studentDBHelper.createStudent(newStudent)

            // Menampilkan hasil operasi
            if (result > -1) {
                Toast.makeText(this, "Sukses menambahkan data dengan primary key: $result", Toast.LENGTH_SHORT).show()
                finish() // Menutup activity setelah berhasil
            } else {
                Toast.makeText(this, "Gagal menambahkan data dengan NIM: $nim", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        // Menutup koneksi database saat activity dihancurkan
        studentDBHelper.close()
        super.onDestroy()
    }
}
