package ru.kpfu.itis.nikolaev.delivery


import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.databinding.ActivityMainBinding
import kotlin.random.Random
import by.kirich1409.viewbindingdelegate.viewBinding


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewBinding by viewBinding(ActivityMainBinding::bind)
    private var navHost: NavHostFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}