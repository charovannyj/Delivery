package ru.kpfu.itis.nikolaev.delivery.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import ru.kpfu.itis.nikolaev.delivery.R
import ru.kpfu.itis.nikolaev.delivery.data.model.OrderModel
import ru.kpfu.itis.nikolaev.delivery.databinding.FragmentMainBinding
import ru.kpfu.itis.nikolaev.delivery.presentation.adapters.CustomAdapter
import ru.kpfu.itis.nikolaev.delivery.presentation.viewmodels.MainViewModel


class MainFragment : Fragment() {

    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    private var ordersGet: List<OrderModel>? = null
    private var ordersSend: List<OrderModel>? = null
    private var dataGetting = false
    private var customAdapter: CustomAdapter? = null
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getOrders()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    private fun checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA),12)
        }
        else{
            findNavController().navigate(R.id.scannerFragment)

        }
    }
    var shimmerLayout : ShimmerFrameLayout? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerLayout = requireActivity().findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        observerData()
        with(viewBinding) {
            viewModel.getOrders()

            scanQr.setOnClickListener {
                checkCameraPermission()
            }


            // Инициализируем адаптер с пустыми данными
            //recyclerView.visibility = View.GONE // Изначально скрываем recyclerView
            shimmerLayout?.startShimmerAnimation()
            customAdapter = CustomAdapter({ order ->
                QRDialogFragment(user!!, order).show(childFragmentManager, "qr_dialog")
            }, emptyList()) // Передаем пустой список при инициализации
            val recyclerView: RecyclerView = recyclerView
            recyclerView.adapter = customAdapter
            rbGet.setOnClickListener {
                recyclerView.visibility = View.VISIBLE
                shimmerLayout!!.visibility = View.VISIBLE
                shimmerLayout?.startShimmerAnimation()
                viewModel.getOrdersGet()
                //shimmerLayout?.stopShimmerAnimation()
                //customAdapter?.updateOrders(ordersGet!!)
               // customAdapter?.notifyDataSetChanged()

            }

            rbSend.setOnClickListener {
                shimmerLayout?.startShimmerAnimation()
                shimmerLayout!!.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                viewModel.getOrdersSend()
                //shimmerLayout?.stopShimmerAnimation()
                //customAdapter?.updateOrders(ordersSend!!)
                //customAdapter?.notifyDataSetChanged()

            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observerData() {
        with(viewModel) {
            lifecycleScope.launch {
                ordersGetFlow.collect { orders ->
                    ordersGet = orders.orEmpty() // Используйте orEmpty() для пустого списка
                    // Обновляем адаптер при получении новых данных
                    if (viewBinding.rbGet.isChecked) { // Обновляем, только если rbGet выбран
                        //shimmerLayout?.startShimmerAnimation()
                        shimmerLayout?.stopShimmerAnimation()
                        shimmerLayout!!.visibility = View.GONE

                        customAdapter?.updateOrders(ordersGet!!)
                        customAdapter?.notifyDataSetChanged()
                        // shimmerLayout?.stopShimmerAnimation()

                    }
                }
            }
            lifecycleScope.launch {
                ordersSendFlow.collect { orders ->
                    ordersSend = orders.orEmpty() // Используйте orEmpty() для пустого списка
                    // Обновляем адаптер при получении новых данных
                    if (viewBinding.rbSend.isChecked) { // Обновляем, только если rbSend выбран
                        //shimmerLayout?.startShimmerAnimation()
                        shimmerLayout?.stopShimmerAnimation()
                        shimmerLayout!!.visibility = View.GONE

                        customAdapter?.updateOrders(ordersSend!!)
                        customAdapter?.notifyDataSetChanged()
                        //shimmerLayout?.stopShimmerAnimation()
                    }
                }
            }
        }
    }
}
