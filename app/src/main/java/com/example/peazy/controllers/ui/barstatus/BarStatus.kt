package com.example.peazy.controllers.ui.barstatus

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.peazy.R
import com.example.peazy.models.reserve_table.ReserveTable
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Status
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bar_status_fragment.*
import kotlinx.android.synthetic.main.bar_status_fragment.view.*
import kotlinx.android.synthetic.main.book_table_dialog.view.*
import retrofit2.Response
import java.util.*


class BarStatus : Fragment() {

    companion object {
        fun newInstance() = BarStatus()
    }

    var root: View? = null
    private lateinit var viewModel: BarStatusViewModel
    var TAG = "BarStatus"
    lateinit var progressDialog: ProgressDialog
    var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.bar_status_fragment, container, false)
        sheetBehavior = BottomSheetBehavior.from(root!!.bottom_sheet)

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

        toggle.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected

            when (checkedId) {
                R.id.pickup -> {
                    sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

                }
                R.id.tableservice -> {
                    sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        bt_select_time.setOnClickListener {
            val myCalendar: Calendar = Calendar.getInstance()
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    /*  var hour = hourOfDay
                      var minutes = minute
                      var timeSet = ""
                      if (hour > 12) {
                          hour -= 12
                          timeSet = "PM"
                      } else if (hour === 0) {
                          hour += 12
                          timeSet = "AM"
                      } else if (hour === 12) {
                          timeSet = "PM"
                      } else {
                          timeSet = "AM"
                      }

                      var min: String? = ""
                      if (minutes < 10) min = "0$minutes" else min = java.lang.String.valueOf(minutes)

                      val aTime: String = StringBuilder().append(hour).append(':')
                          .append(min).append(" ").append(timeSet).toString()*/
                    var aTime: String = "" + hourOfDay + ":" + minute
                    bt_select_time.setText("Selected Time is " + aTime)
                }

            TimePickerDialog(
                getActivity(),
                timeSetListener,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity())
            ).show()

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
            progressDialog.dismiss()

            if (reserveTable.status == 200) {

                val bundle = Bundle()
                bundle.putString("tableNo", "" + root!!.table_num.text.toString())
                val navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.barDetailFragment, true)
                    .build()
                Constants.tableNo = root!!.table_num.text.toString()

                /*   findNavController().navigate(
                       R.id.action_barStatus_to_successStatus,
                       bundle,
                       navOptions
                   )*/
                findNavController().navigate(R.id.action_barDetailFragment_to_menuFragment)


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