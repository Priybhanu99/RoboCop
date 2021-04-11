package com.example.robocop20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var on: Button = findViewById(R.id.find_id)
        on.setOnClickListener{
            val intent = Intent(this, findmissing::class.java)
            startActivity(intent)
        }

        var report: Button = findViewById(R.id.report_id)
        report.setOnClickListener{
            val intent = Intent(this, report_missing::class.java)
            startActivity(intent)
        }

        // Write a message to the database
        // Write a message to the database
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("message")

        myRef.setValue("Hello, World!")
    }
}