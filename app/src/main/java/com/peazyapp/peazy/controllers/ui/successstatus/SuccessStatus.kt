package com.peazyapp.peazy.controllers.ui.successstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.peazyapp.peazy.R
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.fragment_success_status.view.*


class SuccessStatus : Fragment() {

    var root: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_success_status, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        try {

            root!!.tablebookname.text = UserPreferenc.getStringPreference(Constants.USER_NAME, "")

        } catch (e: Exception) {
        }
        root!!.ic_close.setOnClickListener {
            this.requireActivity().onBackPressed()
        }
        root!!.bt_odernow.setOnClickListener {
           /* val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.barDetailFragment, true)
                .build()
            var bundle = Bundle()
            bundle.putString("bar_id", "" + bar_id)
            findNavController().navigate(
                R.id.action_successStatus_to_menuFragment,
                bundle,
                navOptions
            )*/
            findNavController().navigate(R.id.action_successStatus_to_nav_home)

        }

    }


}