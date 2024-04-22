package ru.kpfu.itis.nikolaev.delivery.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import ru.kpfu.itis.nikolaev.delivery.databinding.ActivityMainBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.nikolaev.delivery.R


class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost =  supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHost.navController

        NavigationUI.setupActionBarWithNavController(this,navController)
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}