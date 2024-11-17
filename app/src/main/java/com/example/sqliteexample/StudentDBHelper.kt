package com.example.sqliteexample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class StudentDBHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        // Jika Anda mengubah skema database, Anda harus meningkatkan versi database.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"

        // SQL untuk membuat tabel
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${DBContract.UserEntry.TABLE_NAME} (" +
                    "${DBContract.UserEntry.COLUMN_NIM} TEXT PRIMARY KEY, " +
                    "${DBContract.UserEntry.COLUMN_NAME} TEXT, " +
                    "${DBContract.UserEntry.COLUMN_AGE} TEXT)"

        // SQL untuk menghapus tabel
        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${DBContract.UserEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Menjalankan perintah untuk membuat tabel
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Menghapus tabel lama dan membuat tabel baru saat upgrade
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Proses downgrade database, cukup panggil onUpgrade
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun createStudent(student: StudentModel): Long {
        // Mendapatkan repository data dalam mode tulis
        val db = writableDatabase

        // Membuat map baru untuk data, di mana nama kolom adalah kuncinya
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_NIM, student.nim)
        values.put(DBContract.UserEntry.COLUMN_NAME, student.name)
        values.put(DBContract.UserEntry.COLUMN_AGE, student.age)

        // Menyisipkan baris baru dan mengembalikan nilai primary key
        return db.insert(DBContract.UserEntry.TABLE_NAME, null, values)
    }

    fun searchStudentByNIM(nim: String): ArrayList<StudentModel> {
        val students = ArrayList<StudentModel>()
        val db = writableDatabase
        val cursor: Cursor?

        try {
            // Menjalankan query untuk mencari berdasarkan NIM
            cursor = db.rawQuery(
                "SELECT * FROM ${DBContract.UserEntry.TABLE_NAME} WHERE ${DBContract.UserEntry.COLUMN_NIM} = ?",
                arrayOf(nim)
            )
        } catch (e: SQLiteException) {
            // Jika tabel tidak ada, buat tabel
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        cursor.use {
            // Jika ada data, ambil dan tambahkan ke list
            if (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_NAME))
                val age = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_AGE))
                students.add(StudentModel(nim, name, age))
            }
        }
        return students
    }

    fun searchStudentByName(name: String): ArrayList<StudentModel> {
        val students = ArrayList<StudentModel>()
        val db = writableDatabase
        val cursor: Cursor?

        try {
            // Menjalankan query untuk mencari berdasarkan nama
            cursor = db.rawQuery(
                "SELECT * FROM ${DBContract.UserEntry.TABLE_NAME} WHERE ${DBContract.UserEntry.COLUMN_NAME} LIKE ?",
                arrayOf("%$name%")
            )
        } catch (e: SQLiteException) {
            // Jika tabel tidak ada, buat tabel
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        cursor.use {
            // Iterasi melalui cursor untuk mendapatkan data
            while (it.moveToNext()) {
                val nim = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_NIM))
                val name = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_NAME))
                val age = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_AGE))
                students.add(StudentModel(nim, name, age))
            }
        }
        return students
    }

    fun readStudents(): ArrayList<StudentModel> {
        val students = ArrayList<StudentModel>()
        val db = writableDatabase
        val cursor: Cursor?

        try {
            // Menjalankan query untuk membaca semua data mahasiswa
            cursor = db.rawQuery(
                "SELECT * FROM ${DBContract.UserEntry.TABLE_NAME} ORDER BY ${DBContract.UserEntry.COLUMN_NIM}",
                null
            )
        } catch (e: SQLiteException) {
            // Jika tabel tidak ada, buat tabel
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        cursor.use {
            // Iterasi melalui cursor untuk mendapatkan data
            while (it.moveToNext()) {
                val nim = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_NIM))
                val name = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_NAME))
                val age = it.getString(it.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_AGE))
                students.add(StudentModel(nim, name, age))
            }
        }
        return students
    }

    @Throws(SQLiteConstraintException::class)
    fun updateStudent(student: StudentModel): Int {
        // Mendapatkan repository data dalam mode tulis
        val db = writableDatabase

        // Membuat map baru untuk data, di mana nama kolom adalah kuncinya
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_NAME, student.name)
        values.put(DBContract.UserEntry.COLUMN_AGE, student.age)

        // Menentukan baris mana yang akan diperbarui berdasarkan NIM
        val selection = "${DBContract.UserEntry.COLUMN_NIM} = ?"
        val selectionArgs = arrayOf(student.nim)

        // Melakukan pembaruan data
        return db.update(
            DBContract.UserEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteStudent(nim: String): Int {
        // Mendapatkan repository data dalam mode tulis
        val db = writableDatabase

        // Menentukan kondisi untuk menghapus data
        val selection = "${DBContract.UserEntry.COLUMN_NIM} = ?"
        val selectionArgs = arrayOf(nim)

        // Melakukan penghapusan data
        return db.delete(
            DBContract.UserEntry.TABLE_NAME,
            selection,
            selectionArgs
        )
    }
}
