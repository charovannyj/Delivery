package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private val viewBinding : FragmentWelcomeBinding by viewBinding(FragmentWelcomeBinding::bind)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding){
            btnRegistration.setOnClickListener{
                findNavController().navigate(R.id.registrationFragment)
            }
            btnAuth.setOnClickListener {
                findNavController().navigate(R.id.authFragment)
            }
        }
    }
}