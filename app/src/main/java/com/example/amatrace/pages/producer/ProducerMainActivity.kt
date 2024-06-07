package com.example.amatrace.pages.producer
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.amatrace.R
import com.example.amatrace.databinding.ActivityProducerMainBinding
import com.example.amatrace.pages.login.LoginActivity
import com.example.amatrace.pages.supplier.MainSupplierActivity.Companion.HOME_ITEM
import com.example.amatrace.pages.supplier.MainSupplierActivity.Companion.OFFERS_ITEM
import com.example.amatrace.pages.supplier.MainSupplierActivity.Companion.SECTION_ITEM
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ProfileData
import com.example.core.data.source.remote.response.ProfileProducerData
import com.example.core.data.source.remote.response.ProfileProducerResponse
import com.example.core.data.source.remote.response.ProfileResponse
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        fetchProfileData()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_producer_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_ubahprofileProducer, R.id.nav_daftarbatch, R.id.nav_analisis,
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setUpBottomNavigation(navController)

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

    private fun fetchProfileData() {
        val token = myPreference.getAccessToken() ?: return // Get token from shared preferences
        val call = Config.getApiService().getProfileProducer(token)
        call.enqueue(object : Callback<ProfileProducerResponse> {
            override fun onResponse(call: Call<ProfileProducerResponse>, response: Response<ProfileProducerResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val profile = response.body()?.data
                    if (profile != null) {
                        updateUI(profile)
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<ProfileProducerResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun updateUI(profile: ProfileProducerData) {
        val headerView = binding.navView.getHeaderView(0)
        val profileImageView = headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.myCircleImageViewProducer)
        val ownerNameView = headerView.findViewById<TextView>(R.id.OwnernameProducer)
        val emailView = headerView.findViewById<TextView>(R.id.EmailProducer)
        val businessNameView = headerView.findViewById<TextView>(R.id.bisnisProducer)

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
            CurvedBottomNavigation.Model(OFFERS_ITEM, getString(R.string.scan), R.drawable.ic_scan),
            CurvedBottomNavigation.Model(SECTION_ITEM, getString(R.string.stok), R.drawable.ic_stok),
        )
        val bottomNavView = binding.bottomNavView
        bottomNavView.apply {
            bottomNavigationItems.forEach { add(it) }
            setOnClickMenuListener { model ->
                when (model.id) {
                    HOME_ITEM -> navController.navigate(R.id.navigation_dashboard)
                    OFFERS_ITEM -> navController.navigate(R.id.nav_home)
                    SECTION_ITEM -> navController.navigate(R.id.navigation_notifications)
                }
            }
            show(OFFERS_ITEM)
        }
    }

    companion object {
        val HOME_ITEM = R.id.navigation_dashboard
        val OFFERS_ITEM = R.id.nav_home
        val SECTION_ITEM = R.id.navigation_notifications
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