package com.harish.travelappui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import com.harish.travelappui.repository.AuthState
import com.harish.travelappui.viewmodels.UserAuthViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: UserAuthViewModel

    var fusedLocationClient: FusedLocationProviderClient? = null
    var lastLocation: Location? = null
    private val TAG = "LocationProvider"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    var hasLocationRetrieved = false
    var hasDpAdded = false

    val REQUEST_IMAGE_CAPTURE = 100
    val REQUEST_CHOOSE_FROM_GALLERY=101
    private lateinit var imageUri: Uri
    private var uid = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initViewModel()
        uid = viewModel.generateUid()
        setupObservers()
        register_btn.setOnClickListener {
            val name = name_til.editText?.text.toString()
            val username = uesrname_til.editText?.text.toString()
            val email = email_til.editText?.text.toString()
            val password = password_til.editText?.text.toString()
            if(hasLocationRetrieved && hasDpAdded){
                if (name.isEmpty() ||
                    username.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty()
                ) {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                } else {

                    viewModel.createUser(name, username, email, password,lastLocation,uid,imageUri)
                }
            }else{
                Toast.makeText(this, "Something went wrong when adding dp or getting your location.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { picIntent ->
            picIntent.resolveActivity(packageManager).also {
                startActivityForResult(picIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, REQUEST_CHOOSE_FROM_GALLERY)
    }

    private fun uplaodAndSaveImageUrl(imageBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val id = (500..1000).random()
        val storageRef = FirebaseStorage.getInstance().reference.child("pics/$uid")

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val upload = storageRef.putBytes(baos.toByteArray())

        upload.addOnCompleteListener { uploadTask ->

            if (uploadTask.isSuccessful) {



                storageRef.downloadUrl.addOnCompleteListener { downloadURLTask ->
                    downloadURLTask.result?.let {
                        imageUri = it
                        Log.e("dp uri", it.toString())
                       // iv_profile_image.setImageBitmap(imageBitmap)
                        Toast.makeText(this, "Added dp", Toast.LENGTH_SHORT).show()
                        hasDpAdded = true
                    }

                }

                GlideApp.with(this)
                    .load(storageRef)
                    .into(iv_profile_image)




            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CHOOSE_FROM_GALLERY) {

            imageUri = data?.data!!
            try {
               val bitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver, imageUri
                )
                uplaodAndSaveImageUrl(bitmap)

            } catch (e: Exception) {
               Log.e("GALLERYEXC","$e")
            }


        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uplaodAndSaveImageUrl(imageBitmap)
        }
    }


    private fun setupObservers() {
        viewModel.apply {
            authState.observe(this@RegisterActivity, Observer {
                when (it) {
                    AuthState.REG_SUCCESS -> {
                        progressDialog.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, "Registerd", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@RegisterActivity,HomeV2::class.java))
                    }
                    AuthState.REG_FAILED -> {
                        progressDialog.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    AuthState.REG_INIT -> {
                        progressDialog.visibility = View.VISIBLE

                    }
                }
            })
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(UserAuthViewModel::class.java)
    }

    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        try{
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions()
                return
            }
            fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    Log.e(TAG,"LAT ${lastLocation?.latitude}  LONG ${lastLocation?.longitude}")
                    hasLocationRetrieved=true

                }
                else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e:Exception){
            Log.e(TAG,"Exception in getLastLoc $e")
        }

    }
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@RegisterActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@RegisterActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    fun onAddDp(view: View) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Show us your Smile")
            .setMessage("Get your image from ")
            .setCancelable(false)
            .setNegativeButton("Gallery") { dialog, which ->
                openGallery()
            }
            .setPositiveButton("Take Picture") { dialog, which ->
                takePicture()
            }
            .show()
    }

}