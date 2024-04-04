package ru.kpfu.itis.nikolaev.delivery


import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.databinding.ActivityMainBinding
import kotlin.random.Random
import by.kirich1409.viewbindingdelegate.viewBinding


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController : NavController
    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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