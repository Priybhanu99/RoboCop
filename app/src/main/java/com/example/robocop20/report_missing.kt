package com.example.robocop20

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase


class user{
    var name:String? = null
    var age:String? = null
    var height:String? = null
    var location:String? = null
    var phone_no:String? = null

    constructor(){

    }

    constructor(username: String?, age: String?,
        height: String?,
        location: String?,
        phone_no: String?
    ){
        this.name = username
        this.age = age
        this.location = location
        this.height = height
        this.phone_no = phone_no
    }
}


class report_missing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_missing)


        val database = FirebaseDatabase.getInstance()

        var button:Button = findViewById(R.id.upload)
        button.setOnClickListener{
            dothis()
        }
    }

    private fun dothis() {
        val name:EditText = findViewById(R.id.enter_name)
        val age:EditText = findViewById(R.id.enter_age)
        val height:EditText = findViewById(R.id.enter_height)
        val address:EditText = findViewById(R.id.enter_location)
        val phone_no:EditText = findViewById(R.id.enter_phoneno)

        val child_ = user(
            name.text.toString(),
            age.text.toString(),
            height.text.toString(),
            address.text.toString(),
            phone_no.text.toString(),
        )

        val database = FirebaseDatabase.getInstance()
        val x:String?=  database.getReference("users").push().getKey()
//        val myRef = database.getReference("users").child(x)
        if (x != null) {
            database.getReference("users").child(x).setValue(child_)
        }



    }
}
