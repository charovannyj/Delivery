package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.initialize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentAuthBinding
import ru.kpfu.itis.nikolaev.delivery.model.user.UserSignInModel
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.AuthViewModel


class AuthFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private val viewBinding: FragmentAuthBinding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        observerData()
        with(viewBinding) {
            btnEnter.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val user = UserSignInModel(email, password)
                viewModel.signInWithEmailAndPassword(user)
            }
        }

    }

    private fun observerData() {
        with(viewModel) {

            lifecycleScope.launch {
                currentUserFlow.collect { authResult ->
                    authResult?.let {
                        if (it) {
                            findNavController().navigate(R.id.thirdFragment)
                            Toast.makeText(requireContext(), "Auth yes", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Auth no", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}