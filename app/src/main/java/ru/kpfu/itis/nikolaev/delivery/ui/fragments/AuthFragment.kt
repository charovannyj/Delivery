package ru.kpfu.itis.nikolaev.delivery.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.repository.UsersRepository
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentAuthBinding
import ru.kpfu.itis.nikolaev.delivery.model.user.UserSignUpModel


class AuthFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private val viewBinding : FragmentAuthBinding by viewBinding(FragmentAuthBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Firebase.initialize(requireContext())
        auth = FirebaseAuth.getInstance()
        with(viewBinding){

            btnEnter.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Authentication yes.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            lifecycleScope.launch(Dispatchers.IO) {
                                withContext(Dispatchers.Main) {
                                    findNavController().navigate(R.id.thirdFragment)
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }


            }
        }

    }
}