package com.example.peazy.controllers.ui.addcart.ui.main

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peazy.R
import com.example.peazy.controllers.ui.addcart.AddCartAdepter
import com.example.peazy.databinding.MainFragment3Binding
import com.example.peazy.models.addcart.Add_Item
import com.example.peazy.models.payorder.PayOrder
import com.example.peazy.models.verifypay.VerifyPay
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.util.*


class AddCartFragment : Fragment() {

    companion object {
        fun newInstance() = AddCartFragment()
    }

    val TAG: String = "AddCartFragment"
    var sub_total: Double = 0.0
    var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    var sheetBehavior1: BottomSheetBehavior<LinearLayout>? = null
    private lateinit var viewModel: MainViewModel
    lateinit var binding: MainFragment3Binding
    lateinit var progressDialog: ProgressDialog
    var addcartlist = ArrayList<Add_Item>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment3, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        try {
            addcartlist = Constants.addcartlist

            updateOrderCalculation()

            binding.recleviewAdditem.layoutManager =
                LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)

            viewModel.sub_total.observe(this, Observer {
            })

            binding.tableNo.text = "" + Constants.tableNo
            binding.recleviewAdditem.adapter =
                AddCartAdepter(
                    Constants.addcartlist,
                    { selsectItem: Add_Item -> listItemClick(selsectItem) })

            sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            sheetBehavior1 = BottomSheetBehavior.from(binding.bottomSheet2)
            sheetBehavior!!.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.coordinatorLayout1.visibility = View.GONE
                            binding.coordinatorLayout2.visibility = View.VISIBLE
                            sheetBehavior1!!.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                        }
                        BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> sheetBehavior!!.setHideable(
                            false
                        )
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

            sheetBehavior1!!.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.coordinatorLayout2.visibility = View.GONE
                            binding.coordinatorLayout1.visibility = View.VISIBLE
                            binding.totalPrice.text = Constants.currency + viewModel._total.value
                            binding.totalPrice1.text = Constants.currency + viewModel._total.value
                            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.coordinatorLayout2.visibility = View.GONE
                            binding.coordinatorLayout1.visibility = View.VISIBLE
                            binding.totalPrice.text = Constants.currency + viewModel._total.value
                            binding.totalPrice1.text = Constants.currency + viewModel._total.value
                            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                        BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> sheetBehavior1!!.setHideable(
                            true
                        )
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
            binding.txtaddmore.setOnClickListener {

                this.requireActivity().onBackPressed()

            }



            binding.btPlaceOrder1.setOnClickListener {
                oderConfirm()
            }
            binding.btPlaceOrder.setOnClickListener {
                oderConfirm()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        binding.lifecycleOwner = this
        binding.myorder = viewModel

    }

    fun oderConfirm() {
        var jsonOb = JSONObject()
        var jsonArray = JSONArray()
        for (item in addcartlist) {
            jsonOb.put("item_id", item.item_id)
            jsonOb.put("quantity", item.num_of_unit)
            jsonOb.put("price", item.price.toInt())
            jsonArray.put(jsonOb)
        }
        Log.d(TAG, "Item list" + jsonArray.toString())
        var amount: Int = viewModel._total.value!!.toInt()
        val params = mapOf(
            "bar_id" to "" + Constants.bar_id,
            "amount" to "" + amount,
            "item" to "" + jsonArray.toString()
        )
        Log.d(TAG, "request json" + params)
        setObservers(params)

    }

    fun listItemClick(addItem: Add_Item) {
        updateOrderCalculation()
    }

    fun updateOrderCalculation() {
        viewModel.sub_total.value = 0.0
        for (addItem in Constants.addcartlist) {
            viewModel.sub_total.value =
                viewModel.sub_total.value!! + addItem.num_of_unit * addItem.price.toDouble()
        }

        sub_total = viewModel.sub_total.value!!
        binding.subtotal.text = Constants.currency + viewModel.sub_total.value
        binding.txtvat.text = "" + Constants.vat + "%"
        /*if service charge calculate then remove bellow line*/
        viewModel._total.value = sub_total
        binding.totalPrice.text = "" + viewModel._total.value
        binding.totalPrice1.text = "" + viewModel._total.value
    }

    fun serviceChargeCal() {
        var servicecharge: Double = ((sub_total * Constants.servicefree) / 100)
        if (servicecharge >= 2) {
            viewModel._total.value = sub_total + 2
        } else {
            viewModel._total.value = sub_total + servicecharge

        }
    }

    private fun setObservers(params: Map<String, String>) {
        try {
            viewModel.payOrder(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<PayOrder> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<PayOrder> ->
                                        response.body().toString()
                                    })
                            }
                            com.example.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this.requireContext())

                                progressDialog!!.setMessage("loading...")
                                progressDialog!!.show()


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun sendResponse(payOrder: PayOrder) {
        try {
            progressDialog!!.dismiss()

            if (payOrder.status == 200) {
                var bundle = Bundle()
                bundle.putString(Constants.totalamount, binding.totalPrice.text.toString())
                Constants.stripToken = payOrder.res.stripe_token.toString()
                findNavController().navigate(
                    R.id.action_addCartFragment_to_paymentMethodFragment,
                    bundle
                )
                /* if(!payOrder.res.stripe_token.isEmpty())
                 setconfirmpayObservers(payOrder.res.stripe_token.toString())*/

            } else {
                val errors = payOrder.err

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

    private fun setconfirmpayObservers(striptoken: String) {
        try {
            viewModel.confirmPay(striptoken)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<VerifyPay> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponseForConfirm(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<VerifyPay> ->
                                        response.body().toString()
                                    })
                            }
                            com.example.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this.requireContext())

                                progressDialog!!.setMessage("loading...")
                                progressDialog!!.show()


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun sendResponseForConfirm(verifyPay: VerifyPay) {
        try {
            progressDialog!!.dismiss()

            if (verifyPay.status == 200) {
                var bundle = Bundle()
                bundle.putString(Constants.totalamount, binding.totalPrice.text.toString())
                findNavController().navigate(
                    R.id.action_addCartFragment_to_paymentMethodFragment,
                    bundle
                )


            } else {
                val errors = verifyPay.err

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


}