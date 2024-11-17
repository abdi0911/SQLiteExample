package com.example.sqliteexample

import android.provider.BaseColumns

object DBContract {
    // Kelas dalam yang mendefinisikan struktur tabel
    class UserEntry : BaseColumns {
        companion object {
            // Nama tabel di database
            const val TABLE_NAME = "students"

            // Kolom untuk NIM
            const val COLUMN_NIM = "nim"

            // Kolom untuk Nama
            const val COLUMN_NAME = "name"

            // Kolom untuk Umur
            const val COLUMN_AGE = "age"
        }
    }
}
