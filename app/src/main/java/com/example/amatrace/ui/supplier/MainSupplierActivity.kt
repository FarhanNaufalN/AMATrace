package com.example.amatrace.ui.supplier

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityMainSupplierBinding
import com.example.amatrace.ui.login.LoginActivity
import com.example.core.data.remote.preferences.Preference

class MainSupplierActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainSupplierBinding
    private lateinit var myPreference: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myPreference = Preference(this)

        binding = ActivityMainSupplierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainSupplier.toolbar)

        binding.appBarMainSupplier.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main_supplier)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_ubahprofile, R.id.nav_pengiriman, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView = navView.getHeaderView(0)
        val profileImageView = headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.imageView)
        Glide.with(this)
            .load(R.drawable.profil_farhan)
            .apply(RequestOptions.circleCropTransform())// Replace with the URL or drawable resource
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

    fun logout(item: MenuItem) {
        // Clear token dan status login dari SharedPreferences
        myPreference.clearUserToken()
        myPreference.clearUserLogin()

        // Redirect ke halaman login
        redirectToLogin()
    }
}