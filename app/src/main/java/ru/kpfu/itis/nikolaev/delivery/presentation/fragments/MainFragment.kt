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
    private var customAdapter: CustomAdapter? = null
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getOrders()
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
        shimmerLayout = requireActivity().findViewById(R.id.shimmer_view_container)
        observerData()
        with(viewBinding) {
            viewModel.getOrders()
            scanQr.setOnClickListener {
                checkCameraPermission()
            }
            shimmerLayout?.startShimmerAnimation()
            customAdapter = CustomAdapter({ order ->
                QRDialogFragment(user!!, order).show(childFragmentManager, "qr_dialog")
            }, emptyList())
            val recyclerView: RecyclerView = recyclerView
            recyclerView.adapter = customAdapter
            rbGet.setOnClickListener {
                recyclerView.visibility = View.VISIBLE
                shimmerLayout!!.visibility = View.VISIBLE
                shimmerLayout?.startShimmerAnimation()
                viewModel.getOrdersGet()
            }

            rbSend.setOnClickListener {
                shimmerLayout?.startShimmerAnimation()
                shimmerLayout!!.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                viewModel.getOrdersSend()
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observerData() {
        with(viewModel) {
            lifecycleScope.launch {
                ordersGetFlow.collect { orders ->
                    ordersGet = orders.orEmpty()
                    if (viewBinding.rbGet.isChecked) {
                        shimmerLayout?.stopShimmerAnimation()
                        shimmerLayout!!.visibility = View.GONE
                        customAdapter?.updateOrders(ordersGet!!)
                        customAdapter?.notifyDataSetChanged()
                    }
                }
            }
            lifecycleScope.launch {
                ordersSendFlow.collect { orders ->
                    ordersSend = orders.orEmpty()
                    if (viewBinding.rbSend.isChecked) {
                        shimmerLayout?.stopShimmerAnimation()
                        shimmerLayout!!.visibility = View.GONE
                        customAdapter?.updateOrders(ordersSend!!)
                        customAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
