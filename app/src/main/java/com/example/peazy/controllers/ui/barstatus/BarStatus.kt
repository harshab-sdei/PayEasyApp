package com.example.peazy.controllers.ui.barstatus

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.peazy.R
import kotlinx.android.synthetic.main.bar_status_fragment.view.*

class BarStatus : Fragment() {

    companion object {
        fun newInstance() = BarStatus()
    }

    var root: View? = null
    private lateinit var viewModel: BarStatusViewModel

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.bar_status_fragment, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BarStatusViewModel::class.java)
        root!!.bt_odernow.setOnClickListener {
            findNavController().navigate(R.id.action_barStatus_to_successStatus)
        }

    }

}