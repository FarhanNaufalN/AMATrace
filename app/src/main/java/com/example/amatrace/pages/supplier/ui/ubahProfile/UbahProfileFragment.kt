package com.example.amatrace.pages.supplier.ui.ubahProfile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amatrace.R
import com.example.amatrace.databinding.FragmentUbahprofileBinding
import com.example.amatrace.pages.supplier.MainSupplierActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UbahProfileFragment : Fragment() {

    private var _binding: FragmentUbahprofileBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPreference: Preference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUbahprofileBinding.inflate(inflater, container, false)
        myPreference = Preference(requireContext())
        val root: View = binding.root

        fetchProfileData()

        binding.buttonSave.setOnClickListener {
            saveProfileData()
        }

        return root
    }

    private fun fetchProfileData() {
        val token = myPreference.getAccessToken() ?: return
        val call = Config.getApiService().getProfileSupplier(token)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val profile = response.body()?.data
                    if (profile != null) {
                        binding.editTextOwnerName.setText(profile.ownerName)
                        binding.editTextBusinessName.setText(profile.businessName)
                        binding.editTextPhoneNumber.setText(profile.noHp)
                        binding.editTextAddress.setText(profile.address)
                        binding.editTextDescription.setText(profile.description)

                        Glide.with(this@UbahProfileFragment)
                            .load(profile.avatar)
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.profil_farhan)
                            .error(R.drawable.profil_farhan)
                            .into(binding.profileImageView)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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

        // Construct JSON object
        val jsonObject = JSONObject()
        jsonObject.put("ownerName", ownerName)
        jsonObject.put("businessName", businessName)
        jsonObject.put("noHp", phoneNumber)
        jsonObject.put("address", address)
        jsonObject.put("description", description)

        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val call = Config.getApiService().updateProfileSupplier(token, requestBody)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), MainSupplierActivity::class.java))
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
