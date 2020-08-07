package com.example.peazy.controllers.ui.addcart.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peazy.R
import com.example.peazy.controllers.ui.addcart.AddCartAdepter
import com.example.peazy.databinding.MainFragment3Binding
import com.example.peazy.models.addcart.Add_Item
import com.example.peazy.utility.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.ArrayList

class AddCartFragment : Fragment() {

    companion object {
        fun newInstance() = AddCartFragment()
    }

    var sub_total: Double = 0.0
    var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    var sheetBehavior1: BottomSheetBehavior<LinearLayout>? = null
    private lateinit var viewModel: MainViewModel
    lateinit var binding: MainFragment3Binding
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
                findNavController().navigate(R.id.action_addCartFragment_to_paymentMethodFragment)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        binding.lifecycleOwner = this
        binding.myorder = viewModel

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
        binding.servicefee.text = "" + Constants.servicefree + "%"
        var servicecharge: Double = ((sub_total * Constants.servicefree) / 100)
        if (servicecharge >= 2) {
            viewModel._total.value = sub_total + 2
        } else {
            viewModel._total.value = sub_total + servicecharge

        }

        binding.totalPrice.text = "" + viewModel._total.value
        binding.totalPrice1.text = "" + viewModel._total.value
    }

}