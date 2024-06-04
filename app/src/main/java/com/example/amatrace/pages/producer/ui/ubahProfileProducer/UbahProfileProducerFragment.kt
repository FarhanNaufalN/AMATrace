package com.example.amatrace.pages.producer.ui.ubahProfileProducer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amatrace.R
import com.example.amatrace.databinding.FragmentUbahprofileBinding
import com.example.amatrace.databinding.FragmentUbahprofileproducerBinding
import com.example.amatrace.pages.supplier.MainSupplierActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ProfileResponse
import com.example.core.data.source.remote.response.UploadImageProfileSupplierResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UbahProfileProducerFragment : Fragment(R.layout.fragment_ubahprofile) {

    private var _binding: FragmentUbahprofileBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPreference: Preference
    private var uploadedImageUrl: String? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permissions required to access camera and storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreference = Preference(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUbahprofileBinding.bind(view)
        checkPermissions()
        fetchProfileData()

        binding.buttonSave.setOnClickListener {
            saveProfileData()
        }

        binding.profileImageView.setOnClickListener {
            selectImage()
        }

    }

    private fun selectImage() {
        val options = arrayOf("Ambil dari Galeri", "Ambil dari Kamera")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Sumber Gambar")
        builder.setItems(options) { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> {
                    // Ambil dari galeri
                    openGallery()
                }
                1 -> {
                    // Ambil dari kamera
                    openCamera()
                }
            }
            dialog.dismiss()
        }
        builder.show()
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    data?.data?.let { imageUri ->
                        uploadImageToServer(imageUri)
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load image from gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                CAPTURE_IMAGE_REQUEST -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageBitmap?.let {
                        val imageUri = saveImageToGallery(it)
                        uploadImageToServer(imageUri)
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "Failed to capture image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun saveImageToGallery(bitmap: Bitmap): Uri {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Add image to gallery
            MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                imageFile.absolutePath,
                imageFile.name,
                null
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(imageFile)
    }


    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun uploadImageToServer(uri: Uri) {
        val token = myPreference.getAccessToken() ?: return
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        val file: File = createImageFile()
        val outputStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        Config.getApiService().uploadImageProfileSupplier(token, body)
            .enqueue(object : Callback<UploadImageProfileSupplierResponse> {
                override fun onResponse(
                    call: Call<UploadImageProfileSupplierResponse>,
                    response: Response<UploadImageProfileSupplierResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && responseBody.success) {
                            // Hapus karakter escape backslash dari URL
                            uploadedImageUrl = responseBody.data.image.replace("\\", "")
                            println(uploadedImageUrl)
                            // Update image view with uploaded image
                            Glide.with(this@UbahProfileProducerFragment)
                                .load(uploadedImageUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.profil_farhan)
                                .error(R.drawable.profil_farhan)
                                .into(binding.profileImageView)

                            with(binding) {
                                etAvatar.setText(responseBody.data.image)
                            }

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to upload image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to connect to server",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<UploadImageProfileSupplierResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to connect to server: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    private fun fetchProfileData() {
        val token = myPreference.getAccessToken() ?: return
        Config.getApiService().getProfileSupplier(token)
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val profile = response.body()?.data
                        if (profile != null) {
                            with(binding) {
                                editTextOwnerName.setText(profile.ownerName)
                                editTextBusinessName.setText(profile.businessName)
                                editTextPhoneNumber.setText(profile.noHp)
                                editTextAddress.setText(profile.address)
                                editTextDescription.setText(profile.description)
                                etAvatar.setText(profile.avatar)

                                Glide.with(this@UbahProfileProducerFragment)
                                    .load(profile.avatar)
                                    .apply(RequestOptions.circleCropTransform())
                                    .placeholder(R.drawable.profil_farhan)
                                    .error(R.drawable.profil_farhan)
                                    .into(profileImageView)
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to fetch profile",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun saveProfileData() {
        val token = myPreference.getAccessToken() ?: return
        val ownerName = binding.editTextOwnerName.text.toString()
        val businessName = binding.editTextBusinessName.text.toString()
        val phoneNumber = binding.editTextPhoneNumber.text.toString()
        val address = binding.editTextAddress.text.toString()
        val description = binding.editTextDescription.text.toString()
        val avatar = binding.etAvatar.text.toString()

        val jsonObject = JSONObject().apply {
            put("ownerName", ownerName)
            put("businessName", businessName)
            put("noHp", phoneNumber)
            put("address", address)
            put("description", description)
            put("avatar", avatar)
        }

        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        Config.getApiService().updateProfileSupplier(token, requestBody)
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(requireContext(), MainSupplierActivity::class.java))
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val CAPTURE_IMAGE_REQUEST = 2
        private const val PERMISSIONS_REQUEST_CODE = 123
    }

}