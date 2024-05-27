package com.example.amatrace.ui.producer
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
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
import com.example.amatrace.databinding.ActivityProducerMainBinding
import com.example.amatrace.ui.login.LoginActivity
import com.example.core.data.remote.preferences.Preference

class ProducerMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProducerMainBinding
    private lateinit var myPreference: Preference
    private lateinit var OwnernameProducer : TextView
    private lateinit var EmailProducer : TextView
    private lateinit var BisnisProducer : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        myPreference = Preference(this)

     binding = ActivityProducerMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.appBarProducerMain.toolbar)

        binding.appBarProducerMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_producer_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_ubahprofileProducer, R.id.nav_stockpasokan, R.id.nav_daftarbatch, R.id.nav_analisis   ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView = navView.getHeaderView(0)
        val profileImageView = headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.myCircleImageViewProducer)
        val account = myPreference.getAccountInfo() //Mengambil dari Account Class
        val avatarUrl = account?.avatar
        val name = account?.ownerName
        val email = account?.email
        val bisnis = account?.businessName

        OwnernameProducer= headerView.findViewById(R.id.OwnernameProducer)
        EmailProducer = headerView.findViewById(R.id.EmailProducer)
        BisnisProducer = headerView.findViewById(R.id.bisnisProducer)

        OwnernameProducer.text = name
        EmailProducer.text = email
        BisnisProducer.text = bisnis

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.producer_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_producer_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}