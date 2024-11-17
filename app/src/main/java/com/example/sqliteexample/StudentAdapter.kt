package com.example.sqliteexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sqliteexample.databinding.ItemListStudentBinding

class StudentAdapter(private val studentList: ArrayList<StudentModel>) :
    RecyclerView.Adapter<StudentAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    // Setter untuk callback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // Interface untuk menangani klik item
    interface OnItemClickCallback {
        fun onItemClicked(data: StudentModel)
    }

    // Menangani pembuatan ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    // Menangani pengikatan data dengan ViewHolder
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val student = studentList[position]
        holder.binding.tvNIM.text = "NIM : ${student.nim}"
        holder.binding.tvNama.text = "Nama : ${student.name}"

        // Menambahkan listener pada itemView untuk menangani klik
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(student)
        }
    }

    // Mengembalikan jumlah item dalam daftar
    override fun getItemCount(): Int = studentList.size

    // ViewHolder untuk item list
    inner class ListViewHolder(val binding: ItemListStudentBinding) : RecyclerView.ViewHolder(binding.root)
}