package ru.kpfu.itis.nikolaev.delivery.presentation


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.nikolaev.delivery.Keys
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment)
                .navController
        NavigationUI.setupWithNavController(viewBinding.navView, navController, false)
        navView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}