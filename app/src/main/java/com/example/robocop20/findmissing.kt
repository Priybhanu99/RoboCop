package com.example.robocop20

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import java.util.*


class findmissing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findmissing)

        var upload: Button = findViewById(R.id.upload_to_find)
        upload.setOnClickListener {

            selectImageInAlbum()
        }
    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 0)
        }
    }

    fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
//            imageView.setImageURI(null)
            var img: ImageView = findViewById(R.id.image_id)

            img.setImageURI(data?.data)

            val imageUri: Uri? = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            Log.i("MainActivity", bitmap.toString())
            Log.i("MainActivity", imageUri.toString())

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("images")

            uploadImageToFirebase(imageUri)
//            var storage: FirebaseStorage
//            var storageReference: StorageReference
//
//            storage = FirebaseStorage.getInstance();
//            storageReference = storage.getReference();





        }

    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val fileName = UUID.randomUUID().toString() +".jpg"

            val database = FirebaseDatabase.getInstance()
            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

            if (imageUri != null) {
                refStorage.putFile(imageUri)
                        .addOnSuccessListener(
                                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                        val imageUrl = it.toString()
                                        Log.i("MainActivity",imageUrl)
                                    }
                                })

                        ?.addOnFailureListener(OnFailureListener { e ->
                            print(e.message)
                        })
            }
        }
    }
}