package com.example.peazy.controllers.ui.bardetail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.peazy.R
import com.example.peazy.controllers.ui.bardetail.slidephoto_viewpager.GalleryFragment
import com.example.peazy.controllers.ui.bardetail.slidephoto_viewpager.ViewPagerAdapter
import com.example.peazy.models.booktable.BookTable
import com.example.peazy.models.nearby.Bar
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Status
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.book_table_dialog.view.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BarDetailFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = BarDetailFragment()
        var num_person: Int = 0
    }

    private lateinit var adapter: ViewPagerAdapter
    private var fragments: List<Fragment> = ArrayList()

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var root: View
    var bar_detail = ArrayList<Bar>()
    var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    lateinit var progressDialog: ProgressDialog
    var TAG = "BarDetailFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.main_fragment, container, false)
        return root

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.barmaps) as SupportMapFragment?
        try {
            bar_detail =
                (requireArguments().getSerializable("bardata") as ArrayList<Bar>?)!!
            root.bartitle.text = "" + bar_detail.get(0).name
            root._rating.setText("" + bar_detail.get(0).vat)
            root.palce_name.setText(
                "" + bar_detail.get(0).address.street + "," + bar_detail.get(
                    0
                ).address.city
            )
            root.review.setText("" + bar_detail.get(0).total_reviews + " Reviews")
            root.country.setText("Commission " + bar_detail!!.get(0).p_commission + " ")
            root.detail.setText("" + bar_detail!!.get(0).description)
            root.time.setText("Open . " + bar_detail.get(0).hours)
            Constants.vat = bar_detail.get(0).vat.toDouble()
            Constants.bar_id = bar_detail.get(0).bar_id
            val bt_booknow = root.findViewById<Button>(R.id.book_table) as Button
            bt_booknow.setOnClickListener {
                showDialog()
            }
            mapFragment!!.getMapAsync(this)
            root.bt_order.setOnClickListener {

                findNavController().navigate(R.id.action_barDetailFragment_to_menuFragment)

            }
            sheetBehavior = BottomSheetBehavior.from(root.bottom_sheet)

            sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

            fragments = getFragments()
            adapter = ViewPagerAdapter(this.childFragmentManager, fragments)
            viewPager.adapter = adapter
            dots_indicator.setViewPager(viewPager)

            adapter.notifyDataSetChanged()
        } catch (e: java.lang.Exception) {
        }
    }

    private fun getFragments(): List<Fragment> {
        val fList = ArrayList<Fragment>()
        for (i in bar_detail.get(0).image) {
            fList.add(GalleryFragment.newInstance(i))
        }
        return fList
    }

    fun showDialog() {
        val mDialogView =
            LayoutInflater.from(this.requireContext()).inflate(R.layout.book_table_dialog, null)
        val mBuilder = AlertDialog.Builder(this.requireContext())
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        val timeSetListener =
            OnTimeSetListener { view, hourOfDay, minute ->
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
                mDialogView.txttime.setText(aTime)
            }
        val myCalendar: Calendar = Calendar.getInstance()
        updateLabel(mDialogView.txtdate, myCalendar)
        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(mDialogView.txtdate, myCalendar)


                TimePickerDialog(
                    getActivity(),
                    timeSetListener,
                    myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE),
                    false
                ).show()
            }

        mDialogView.bt_calender.setOnClickListener {

            DatePickerDialog(
                this.requireContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        mDialogView.bt_close.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
        val edit = mDialogView.ed_num_person
        mDialogView.bt_add.setOnClickListener {
            num_person++
            edit.setText("" + num_person)
        }
        mDialogView.bt_minus.setOnClickListener {
            if (num_person > 0)
                num_person--

            edit.setText("" + num_person)

        }
        mDialogView.bt_booknow.setOnClickListener {
            mAlertDialog.dismiss()
            sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val params = mapOf(
                        "bar_id" to bar_detail.get(0).bar_id,
                        "date" to mDialogView.txtdate.text.toString(),
                        "time" to mDialogView.txttime.text.toString(),
                        "totalPerson" to mDialogView.ed_num_person.text.toString()
                    )
                    println(params)
                    if (mDialogView.ed_num_person.text.toString().toInt() > 0) {
                        setupObservers(params)

                    } else {
                        setError("Person have to be grater than 0")
                    }
                    root.alramtime.setText("" + millisUntilFinished / 1000)
                }

                override fun onFinish() {


                }
            }.start()

        }
        mDialogView.txttime.setOnClickListener {
            TimePickerDialog(
                getActivity(),
                timeSetListener,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity())
            ).show()

        }


    }

    fun setError(msg: String) {
        sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        AppUtility.getInstance()
            .alertDialogWithSingleButton(
                this.requireContext(),
                "Error",
                "" + msg
            )
    }

    private fun updateLabel(txtview: TextView, myCalendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtview.setText(sdf.format(myCalendar.getTime()))
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        try {
            MapsInitializer.initialize(this.activity)
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
        val icon =
            BitmapDescriptorFactory.fromResource(R.drawable.pin)
        val sydney = LatLng(
            bar_detail.get(0).address.latlong.coordinate.get(1),
            bar_detail.get(0).address.latlong.coordinate.get(0)
        )
        val melbourne = mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("" + bar_detail.get(0).name)
                .snippet(
                    "" + bar_detail.get(0).address.street + ", " + bar_detail.get(0).address.city + ", " + bar_detail.get(
                        0
                    ).address.zip_code
                )
                .icon(icon)
        )
        melbourne.setTag(0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))

    }

    private fun setupObservers(params: Map<String, String>) {
        try {
            viewModel.bookTable(params)
                .observe(this, androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {

                                resource.data?.let { response: Response<BookTable> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<BookTable> ->
                                        response.body().toString()
                                    })
                            }
                            Status.ERROR -> {
                                try {
                                    //   progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            Status.LOADING -> {
                                /*  progressDialog = ProgressDialog(this.requireContext())
                                  progressDialog!!.setMessage("loading...")
                                  progressDialog!!.show()*/
                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun sendResponse(bookTable: BookTable) {
        try {
            //  progressDialog!!.dismiss()

            if (bookTable.status == 200) {

                val bundle = Bundle()
                bundle.putString("tiltle", "" + root.bartitle.text.toString())
                bundle.putString("bar_id", "" + bar_detail.get(0).bar_id)
                findNavController().navigate(R.id.action_barDetailFragment_to_barStatus, bundle)
                sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED


            } else {
                sheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

                val errors = bookTable.err

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