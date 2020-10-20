package com.peazyapp.peazy.controllers.ui.orderhistory

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.ui.confirmorderstatus.adepter.OrderConfirmItemAdepter
import com.peazyapp.peazy.controllers.ui.orderhistory.adepter.OrderHistoryAdepter
import com.peazyapp.peazy.models.oderhistory.OrderHistory
import com.peazyapp.peazy.models.oderhistory.User
import com.peazyapp.peazy.models.verifypay.Item
import com.peazyapp.peazy.models.verifypay.VerifyPay
import com.peazyapp.peazy.utility.AppUtility
import kotlinx.android.synthetic.main.order_history_fragment.*
import retrofit2.Response

class OrderHistoryFragment : Fragment() {
    val TAG = "OrderHistoryFragment"

    companion object {
        fun newInstance() = OrderHistoryFragment()
    }

    lateinit var progressDialog: ProgressDialog
    private lateinit var viewAdapter: OrderHistoryAdepter

    private lateinit var viewModel: OrderHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel::class.java)
        recleview_historyitem.layoutManager =
            LinearLayoutManager(this.requireActivity(), LinearLayoutManager.VERTICAL, false)
        setorderHistoryObservers()
    }


    private fun setorderHistoryObservers() {
        try {
            viewModel.getHistoryList()
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<OrderHistory> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<OrderHistory> ->
                                        response.body().toString()
                                    })
                            }
                            com.peazyapp.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.peazyapp.peazy.utility.Status.LOADING -> {

                                progressDialog = ProgressDialog(this.requireActivity())

                                progressDialog.setMessage("loading...")
                                progressDialog.show()
                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun sendResponse(orderHistory: OrderHistory) {
        try {
            progressDialog.dismiss()

            if (orderHistory.status == 200) {
                viewAdapter =
                    OrderHistoryAdepter(
                        orderHistory.res.users,
                        { selsectItem: User -> listItemClick(selsectItem) })
                recleview_historyitem.adapter = viewAdapter


            } else {
                val errors = orderHistory.err

                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this.requireContext(),
                        "Error",
                        "" + errors.msg
                    )
            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun listItemClick(selsectItem: User) {

    }
}