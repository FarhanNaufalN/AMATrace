package com.example.amatrace.pages.supplier

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityMainSupplierBinding
import com.example.amatrace.pages.login.LoginActivity
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ProfileData
import com.example.core.data.source.remote.response.ProfileResponse
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainSupplierActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainSupplierBinding
    private lateinit var myPreference: Preference
    private lateinit var Ownername: TextView
    private lateinit var Email: TextView
    private lateinit var Bisnis: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)

        binding = ActivityMainSupplierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainSupplier.toolbar)
        fetchProfileData()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView: CurvedBottomNavigation = binding.bottomNavView
        val navController = findNavController(R.id.nav_host_fragment_content_main_supplier)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_ubahprofile, R.id.nav_pengiriman, R.id.nav_logout,
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setUpBottomNavigation(navController)

        val headerView = navView.getHeaderView(0)
        val profileImageView = headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.myCircleImageView)
        val account = myPreference.getAccountInfo() //Mengambil dari Account Class
        val avatarUrl = account?.avatar
        val name = account?.ownerName
        val email = account?.email
        val bisnis = account?.businessName

        Ownername = headerView.findViewById(R.id.Ownername)
        Email = headerView.findViewById(R.id.EmailSupplier)
        Bisnis = headerView.findViewById(R.id.bisnis)

        Ownername.text = name
        Email.text = email
        Bisnis.text = bisnis

        // Muat gambar menggunakan Glide
        Glide.with(this)
            .load(avatarUrl)
            .apply(RequestOptions.circleCropTransform()) // Menggunakan transformasi lingkaran pada gambar
            .placeholder(R.drawable.profil_farhan) // Placeholder jika gambar sedang dimuat
            .error(R.drawable.profil_farhan) // Gambar default jika terjadi kesalahan
            .into(profileImageView)

        // Cek apakah pengguna sebelumnya sudah login atau tidak
        if (!myPreference.getStatusLogin()) {
            // Jika tidak, arahkan ke halaman login
            redirectToLogin()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_supplier, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_supplier)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun redirectToLogin() {
        // Redirect ke halaman login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity() // Tutup semua aktivitas yang terkait dengan aplikasi ini
    }

    private fun fetchProfileData() {
        val token = myPreference.getAccessToken() ?: return // Get token from shared preferences
        val call = Config.getApiService().getProfileSupplier(token)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val profile = response.body()?.data
                    if (profile != null) {
                        updateUI(profile)
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun updateUI(profile: ProfileData) {
        val headerView = binding.navView.getHeaderView(0)
        val profileImageView = headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.myCircleImageView)
        val ownerNameView = headerView.findViewById<TextView>(R.id.Ownername)
        val emailView = headerView.findViewById<TextView>(R.id.EmailSupplier)
        val businessNameView = headerView.findViewById<TextView>(R.id.bisnis)

        Glide.with(this)
            .load(profile.avatar)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.profil_farhan)
            .error(R.drawable.profil_farhan)
            .into(profileImageView)

        ownerNameView.text = profile.ownerName
        businessNameView.text = profile.businessName
    }

    private fun setUpBottomNavigation(navController: NavController) {
        val bottomNavigationItems = mutableListOf(
            CurvedBottomNavigation.Model(HOME_ITEM, getString(R.string.home), R.drawable.ic_home_black_24dp),
            CurvedBottomNavigation.Model(OFFERS_ITEM, getString(R.string.home), R.drawable.ic_home_black_24dp),
            CurvedBottomNavigation.Model(SECTION_ITEM, getString(R.string.home), R.drawable.ic_home_black_24dp),
        )
        val bottomNavView = binding.bottomNavView
        bottomNavView.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener { model ->
                when (model.id) {
                    HOME_ITEM -> navController.navigate(R.id.nav_home)
                    OFFERS_ITEM -> navController.navigate(R.id.navigation_dashboard)
                    SECTION_ITEM -> navController.navigate(R.id.navigation_notifications)

                }
            }
            show(OFFERS_ITEM)
        }
    }


    fun logout(item: MenuItem) {
        // Clear token dan status login dari SharedPreferences
        myPreference.clearUserToken()
        myPreference.clearUserLogin()

        // Redirect ke halaman login
        redirectToLogin()
    }

    companion object {
         val HOME_ITEM = R.id.nav_home
         val OFFERS_ITEM = R.id.navigation_dashboard
        val SECTION_ITEM = R.id.navigation_notifications
    }
}
