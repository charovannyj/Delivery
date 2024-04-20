package ru.kpfu.itis.nikolaev.delivery.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentFirstBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.nikolaev.delivery.R

class FirstFragment : Fragment() {
    private val viewBinding : FragmentFirstBinding by viewBinding(FragmentFirstBinding::bind)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding){
            btn.setOnClickListener{
                findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
            }
        }
    }
}