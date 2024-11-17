package com.example.sqliteexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteexample.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STUDENT = "extra_student"
    }

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var studentDBHelper: StudentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get data from intent
        val studentData = intent.getParcelableExtra<StudentModel>(EXTRA_STUDENT) as StudentModel

        // Set initial data to UI fields
        binding.etNIM.isEnabled = false
        binding.etNIM.setText(studentData.nim)
        binding.etNama.setText(studentData.name)
        binding.etUmur.setText(studentData.age)

        // Initialize DB Helper
        studentDBHelper = StudentDBHelper(this)

        // Update button click listener
        binding.btnUpdate.setOnClickListener {
            val nim = binding.etNIM.text.toString()
            val name = binding.etNama.text.toString()
            val age = binding.etUmur.text.toString()

            // Check if fields are empty
            if (nim.isEmpty() || name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Silakan masukkan data NIM, nama, dan umur!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update student data
            val newUpdateStudent = StudentModel(nim = nim, name = name, age = age)
            val updateCount = studentDBHelper.updateStudent(newUpdateStudent)

            // Show toast message depending on update success
            if (updateCount > 0) {
                Toast.makeText(this, "Mahasiswa yang terupdate: $updateCount", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Tidak ada data mahasiswa yang diupdate. Silakan coba lagi!", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete button click listener
        binding.btnHapus.setOnClickListener {
            // Use AlertDialog for confirmation
            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("Konfirmasi Hapus Data")
                .setMessage("Apakah Anda yakin?")
                .setPositiveButton("Ya") { _, _ ->
                    val nim = binding.etNIM.text.toString()
                    val deleteCount = studentDBHelper.deleteStudent(nim)

                    // Show toast message depending on delete success
                    if (deleteCount > 0) {
                        Toast.makeText(this, "Mahasiswa yang terhapus: $deleteCount", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Tidak ada data mahasiswa yang dihapus. Silakan coba lagi!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Tidak") { _, _ ->
                    // User cancelled the dialog
                }

            // Show the confirmation dialog
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onDestroy() {
        studentDBHelper.close()
        super.onDestroy()
    }
}
