package com.chan.javaroomdemo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class KotlinCaptureImage : AppCompatActivity() {

     private var mCurrentPhotoPath: String?= null

    private var activityResultLauncher: ActivityResultLauncher<Intent>?= null

    private var imageView: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_capture_image)


        val btnTakePhoto: Button = findViewById(R.id.btnTakePhoto)
        imageView = findViewById(R.id.imageView)
        intiActivityLauncher()
        btnTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            try {
                val photoPath: File = createPath()

                if(photoPath.exists()){
                    activityResultLauncher!!.launch(intent)
                }

            }catch (e:IOException){
                e.printStackTrace()
            }
        }

    }

    private fun intiActivityLauncher() {
        activityResultLauncher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result != null && result.resultCode == RESULT_OK){
                val data = result.data;
                if(data != null){

                    try {
                        var bitmap: Bitmap = data.extras!!.get("data") as Bitmap
                        val file = File(mCurrentPhotoPath!!)
                        val outputStream: OutputStream = FileOutputStream(file)
                        bitmap.compress(CompressFormat.JPEG, 100, outputStream)
                        outputStream.flush()

                        imageView!!.setImageBitmap(bitmap)

                    }catch (e : IOException){
                        e.printStackTrace()
                    }catch (ex: FileNotFoundException){
                        ex.printStackTrace()
                    }

                }
            }
        }
    }

    private fun createPath(): File {
        var imageFile: File?= null

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date());
        val imageFileName = "JPEG_${timestamp}_"

        try {
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            imageFile = File.createTempFile(imageFileName,".jpg", storageDir)

            mCurrentPhotoPath = imageFile.absolutePath

        }catch (ex: IOException){
            ex.printStackTrace()
        }

        return imageFile!!
    }
}