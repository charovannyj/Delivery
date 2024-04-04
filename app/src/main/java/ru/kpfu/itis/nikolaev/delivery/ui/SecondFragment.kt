package ru.kpfu.itis.nikolaev.delivery.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentSecondBinding

class SecondFragment : Fragment(R.layout.fragment_second) {
    private val viewBinding: FragmentSecondBinding by viewBinding(FragmentSecondBinding::bind)
    private var btnRoleIsClicked: Boolean = false
    private var etEmailIsTyped: Boolean = false
    private var etPasswordIsTyped: Boolean = false
    private lateinit var button: Button
    fun checkReady() {
        button.isEnabled = btnRoleIsClicked && etEmailIsTyped && etPasswordIsTyped
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailRegex =
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex()

        with(viewBinding) {
            button = btnEnter
            btnEnter.isEnabled = false

            btnClient.setOnClickListener {
                btnRoleIsClicked = true
                btnClient.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_500
                    )
                )
                btnCourier.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_200
                    )
                )
                checkReady()
            }
            btnCourier.setOnClickListener {
                btnRoleIsClicked = true
                btnClient.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_200
                    )
                )
                btnCourier.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_500
                    )
                )
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
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                findNavController().navigate(R.id.action_secondFragment_to_thirdFragment)

            }
        }
    }

}