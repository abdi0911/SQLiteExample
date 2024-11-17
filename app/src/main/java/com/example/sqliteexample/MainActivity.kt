package com.example.sqliteexample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqliteexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentDBHelper: StudentDBHelper
    private var listData: ArrayList<StudentModel> = arrayListOf()
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding and adapter
        binding = ActivityMainBinding.inflate(layoutInflater)
        studentAdapter = StudentAdapter(listData)

        // Set up RecyclerView
        initializeRecyclerList()

        setContentView(binding.root)

        // Initialize database helper
        studentDBHelper = StudentDBHelper(this)

        // Search button listener
        binding.btnCari.setOnClickListener {
            val name = binding.etNama.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Silakan masukkan nama terlebih dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val students = studentDBHelper.searchStudentByName(name)
            listData.clear()
            listData.addAll(students)
            studentAdapter.notifyDataSetChanged()
            binding.tvHasilPencarian.text = "Ditemukan ${students.size} mahasiswa"
        }

        // Add student button listener
        binding.btnTambah.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreateActivity::class.java))
        }

        // SwipeRefresh listener for reloading data
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            loadAllData()
        }
    }

    private fun loadAllData() {
        val students = studentDBHelper.readStudents()
        listData.clear()
        listData.addAll(students)
        studentAdapter.notifyDataSetChanged()
        binding.tvHasilPencarian.text = "Total ${students.size} mahasiswa"
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        loadAllData()
    }

    override fun onDestroy() {
        studentDBHelper.close()
        super.onDestroy()
    }

    private fun initializeRecyclerList() {
        binding.rvStudents.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = studentAdapter
        }

        studentAdapter.setOnItemClickCallback(object : StudentAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StudentModel) {
                updateStudent(data)
            }
        })
    }

    private fun updateStudent(data: StudentModel) {
        val moveWithObjectIntent = Intent(this@MainActivity, UpdateActivity::class.java)
        moveWithObjectIntent.putExtra(UpdateActivity.EXTRA_STUDENT, data)
        startActivity(moveWithObjectIntent)
    }
}
