package ru.kpfu.itis.nikolaev.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentFirstBinding
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private val viewBinding : FragmentSecondBinding by viewBinding(FragmentSecondBinding::bind)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

}