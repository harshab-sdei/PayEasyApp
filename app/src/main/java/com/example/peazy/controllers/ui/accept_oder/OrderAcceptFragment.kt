package com.example.peazy.controllers.ui.accept_oder

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.peazy.R
import com.example.peazy.databinding.OrderAcceptFragmentBinding

class OrderAcceptFragment : Fragment() {


    lateinit var databinding: OrderAcceptFragmentBinding
    private lateinit var viewModel: OrderAcceptViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding =
            DataBindingUtil.inflate(inflater, R.layout.order_accept_fragment, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderAcceptViewModel::class.java)

        databinding.btCheckorder.setOnClickListener {

        }

        databinding.btOrdermore.setOnClickListener {
            findNavController().navigate(R.id.action_orderAcceptFragment_to_menuFragment)

        }
    }

}