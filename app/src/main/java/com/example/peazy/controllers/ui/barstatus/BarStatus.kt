package com.example.peazy.controllers.ui.barstatus

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.peazy.R
import com.example.peazy.models.reserve_table.ReserveTable
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Status
import kotlinx.android.synthetic.main.bar_status_fragment.view.*
import retrofit2.Response

class BarStatus : Fragment() {

    companion object {
        fun newInstance() = BarStatus()
    }

    var root: View? = null
    private lateinit var viewModel: BarStatusViewModel
    var TAG = "BarStatus"
    lateinit var progressDialog: ProgressDialog

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
        var str: String? = requireArguments().getString("tiltle").toString()
        var bar_id: String? = requireArguments().getString("bar_id").toString()
        (activity as? AppCompatActivity)?.supportActionBar?.setTitle("" + str)

        root!!.bt_odernow.setOnClickListener {
            var params = mapOf(
                "bar_id" to bar_id.toString(),
                "table_number" to root!!.table_num.text.toString()
            )
            setupObservers(params)
        }
    }

    private fun setupObservers(params: Map<String, String>) {
        try {
            viewModel.reserveTable(params)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {

                                resource.data?.let { response: Response<ReserveTable> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(
                                                it1
                                            )
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<ReserveTable> ->
                                        response.body().toString()
                                    })
                            }
                            Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            Status.LOADING -> {
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

    fun sendResponse(reserveTable: ReserveTable) {
        try {
            progressDialog!!.dismiss()

            if (reserveTable.status == 200) {

                val bundle = Bundle()
                bundle.putString("tableNo", "" + root!!.table_num.text.toString())
                val navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.barDetailFragment, true)
                    .build()
                Constants.tableNo = root!!.table_num.text.toString()

                findNavController().navigate(
                    R.id.action_barStatus_to_successStatus,
                    bundle,
                    navOptions
                )

            } else {

                val errors = reserveTable.err

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