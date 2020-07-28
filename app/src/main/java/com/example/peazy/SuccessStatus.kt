package com.example.peazy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_success_status.view.*


class SuccessStatus : Fragment() {

    var root: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_success_status, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        try {
            var str: String? = requireArguments().getString("tableNo").toString()
            root!!.tablenum.text = str

        } catch (e: Exception) {
        }
        root!!.bt_odernow.setOnClickListener {
            findNavController().navigate(R.id.action_successStatus_to_menuFragment)
        }

    }


}