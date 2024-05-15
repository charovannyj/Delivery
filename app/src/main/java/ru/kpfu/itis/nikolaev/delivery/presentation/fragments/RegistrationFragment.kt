package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize

import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentRegistrationBinding
import ru.kpfu.itis.nikolaev.delivery.domain.model.UserSignUpModel
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.RegistrationViewModel

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private val viewBinding: FragmentRegistrationBinding by viewBinding(FragmentRegistrationBinding::bind)
    private var btnRoleIsClicked: Boolean = false
    private var etEmailIsTyped: Boolean = false
    private var etPasswordIsTyped: Boolean = false
    private lateinit var button: Button
    val emailRegex =
        "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex()

    fun checkReady() {
        button.isEnabled = btnRoleIsClicked && etEmailIsTyped && etPasswordIsTyped
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var role: String? = null
        with(viewBinding) {
            button = btnEnter

            rbClient.setOnClickListener {
                btnRoleIsClicked = true
                role = "client"
                checkReady()
            }
            rbCourier.setOnClickListener {
                btnRoleIsClicked = true
                role = "courier"

                checkReady()
            }
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        tilEmail.error = "Поле не может быть пустым"
                    } else {
                        if (emailRegex.matches(s.toString())) {
                            etEmailIsTyped = true
                            tilEmail.error = null
                        } else {
                            etEmailIsTyped = false
                            tilEmail.error = "Некорректно введено"
                        }
                    }
                    checkReady()
                }
            })
            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        etPasswordIsTyped = false
                        tilPassword.error = "Поле не может быть пустым"
                    } else {
                        if (s.toString().length < 1) {
                            etPasswordIsTyped = false
                            tilPassword.error = "malo simvolov"
                        } else {
                            etPasswordIsTyped = true
                            tilPassword.error = null
                        }
                    }
                    checkReady()
                }
            })
            btnEnter.setOnClickListener {
                val name = etName.text.toString()
                val surname = etSurname.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val user = UserSignUpModel(role!!, name, surname, email, password)
                viewModel.signUpWithEmailAndPassword(user)

            }
            observerData()
        }
    }

    private fun observerData() {
        with(viewModel) {
            lifecycleScope.launch {
                currentUserFlow.collect { registrationResult ->
                    registrationResult?.let {
                        if (it) {
                            findNavController().navigate(R.id.thirdFragment)
                            Toast.makeText(requireContext(), "Reg yes", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Reg no", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}