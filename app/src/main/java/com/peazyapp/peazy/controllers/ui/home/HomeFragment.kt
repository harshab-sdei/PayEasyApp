package com.peazyapp.peazy.controllers.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.ui.home.adepter.CustomInfoWindowGoogleMap
import com.peazyapp.peazy.controllers.ui.home.adepter.MapBarAdepter
import com.peazyapp.peazy.controllers.ui.menu.MenuFragment
import com.peazyapp.peazy.models.editprofile.InfoWindowData
import com.peazyapp.peazy.models.nearby.Bar
import com.peazyapp.peazy.models.nearby.NearByBar
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.peazyapp.peazy.controllers.ui.menu.Menu
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback, ConnectionCallbacks,
    OnConnectionFailedListener {
    var TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    var locationPermissionGranted: Boolean = false
    private var googleApiClient: GoogleApiClient? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    lateinit var progressDialog: ProgressDialog

    private var employeeLocation: LatLng? = null
    protected var locationManager: LocationManager? = null
    var isGPSEnabled = false
    var isNetworkEnabled = false
    private var permissionsList: ArrayList<String>? = null
    lateinit var mapFragment: SupportMapFragment
    var listbar = ArrayList<Bar>()
    private lateinit var viewAdapter: MapBarAdepter

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                employeeLocation = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(employeeLocation))
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), 15f
                    )
                )
                Log.d(
                    "onLocationResult: ",
                    location.latitude.toString() + " , " + location.longitude
                )
            }
        }
    }


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        try {
            if (Constants.isredirectToMenu == true) {
                Constants.isredirectToMenu = false
                try {
                    Constants.addcartlist.clear()
                } catch (e: Exception) {
                }
                findNavController().navigate(R.id.action_nav_home_to_menuFragment)


            }
        } catch (e: Exception) {
        }

        statusCheck()
        if (!checkLocationPermission()) {

            getPermissions()
        }
        mapFragment = (childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?)!!
        try {
            if (AppUtility.getInstance()
                    .isNetworkAvailable(this.requireActivity())
            ) {
                setNearByObservers("[-110.8571443, 32.4586858]")
            } else {
                AppUtility.getInstance()
                    .alertDialogWithSingleButton(
                        this.requireContext(),
                        "Error",
                        "" + getString(R.string.no_internet_connection)
                    )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (!isGooglePlayServicesAvailable()) {
        }

        root.ed_serch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewAdapter.getFilter().filter(s)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        try {


            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this.requireActivity())


        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val myLocationButton: View = mapFragment.requireView().findViewById(0x2)
            // Specify the types of place data to return.

            if (myLocationButton != null && myLocationButton.layoutParams is RelativeLayout.LayoutParams) {
                // location button is inside of RelativeLayout
                val params: RelativeLayout.LayoutParams =
                    myLocationButton.layoutParams as RelativeLayout.LayoutParams

                // Align it to - parent BOTTOM|LEFT
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)

                // Update margins, set to 10dp
                val margin = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 5F,
                    resources.displayMetrics
                ).toInt()
                params.setMargins(margin, margin, margin, margin)

                myLocationButton.layoutParams = params
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }



        homeViewModel.barlist.observe(this.requireActivity(), androidx.lifecycle.Observer {
            try {
                if (!it.isNullOrEmpty()) {
                    viewAdapter = MapBarAdepter(
                        it
                    ) { selsectItem: Bar -> clickOnListOfBarItem(selsectItem) }

                    val spanCount = 3 // 3 columns

                    // use a linear layout manager
                    root.recycle_mapbar.layoutManager =
                        GridLayoutManager(this.requireActivity(), spanCount)
                    root.recycle_mapbar.adapter = viewAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })


        return root
    }

    private fun clickOnListOfBarItem(selsectItem: Bar) {
        try {
            Constants.addcartlist.clear()
            MenuFragment.itemCount = 0
            MenuFragment.price_total = 0.0

            val bundle = Bundle()
            var list = ArrayList<Bar>()
            list.add(selsectItem)
            bundle.putSerializable("bardata", list)
            findNavController().navigate(R.id.action_nav_home_to_barDetailFragment, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkLocationPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val res: Int = this.requireActivity().checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermissions() {

        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                )
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        try {
            if (locationPermissionGranted) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getPermissions()
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.setMapToolbarEnabled(false)


        try {
            for (i in 0 until listbar.size) {
                createMarker(
                    i,
                    listbar.get(i).address.latlong.coordinate.get(1),
                    listbar.get(i).address.latlong.coordinate.get(0),
                    listbar.get(i).name,
                    listbar.get(i).address.street + ",\n" + listbar.get(i).address.city,
                    4
                )
            }
        } catch (e: Exception) {
        }

        mMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    listbar.get(0).address.latlong.coordinate.get(1),
                    listbar.get(0).address.latlong.coordinate.get(0)
                )
            )
        )
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    listbar.get(0).address.latlong.coordinate.get(1),
                    listbar.get(0).address.latlong.coordinate.get(0)
                ), 8f
            )
        )

        //  mMap.setOnMarkerClickListener(this)

        mMap.setOnInfoWindowClickListener(object : GoogleMap.OnInfoWindowClickListener {
            override fun onInfoWindowClick(marker: Marker) {
                try {
                    Constants.addcartlist.clear()
                    MenuFragment.itemCount = 0
                    MenuFragment.price_total = 0.0

                    val info: InfoWindowData = marker.tag as InfoWindowData
                    val bundle = Bundle()
                    var list = ArrayList<Bar>()

                    list.add(listbar.get(info.tagid))
                    bundle.putSerializable("bardata", list)
                    findNavController().navigate(R.id.action_nav_home_to_barDetailFragment, bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    protected fun createMarker(
        tagid: Int, latitude: Double, longitude: Double,
        title: String?, snippet: String?, rating: Int
    ): Marker? {
        var marker: Marker? = null
        val icon =
            BitmapDescriptorFactory.fromResource(R.drawable.pin)

        /* marker = mMap.addMarker(
             MarkerOptions()
                 .position(LatLng(latitude, longitude))
                 .anchor(0.5f, 0.5f)
                 .title(title)
                 .snippet(snippet)
                 .icon(icon)

         )
         marker.tag = tagid
 */
        val markerOptions = MarkerOptions()
        markerOptions.position(LatLng(latitude, longitude))
            .title(title)
            .snippet(snippet)
            .icon(icon)

        val info = InfoWindowData(tagid, title!!, snippet!!, rating)

        val customInfoWindow = CustomInfoWindowGoogleMap(this.requireContext())
        mMap.setInfoWindowAdapter(customInfoWindow)

        val m = mMap.addMarker(markerOptions)
        m.tag = info
        m.showInfoWindow()

        return m
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun themeNavAndStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        val w: Window = activity.window
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        w.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        w.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        w.setNavigationBarColor(activity.resources.getColor(android.R.color.transparent))
        w.setStatusBarColor(activity.resources.getColor(android.R.color.transparent))
    }

    private fun requestLocationUpdates() {
        try {
            locationRequest = LocationRequest()
            locationRequest!!.setInterval(2000)
            locationRequest!!.setFastestInterval(2000)
            //locationRequest.setSmallestDisplacement(5);
            locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionGranted = true

            } else {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                    )
                }
            }
            if (statusCheck()) {
                fusedLocationProviderClient!!.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnected(p0: Bundle?) {
        TODO("Not yet implemented")
        requestLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        AppUtility.getInstance()
            .showToast(this.requireContext(), getString(R.string.connection_failed))
    }

    override fun onStop() {
        super.onStop()
        if (fusedLocationProviderClient != null) {
            if (statusCheck()) {
                fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        }
        if (listbar.size > 0)
            mapFragment.getMapAsync(this)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
        try {
            setNearByObservers("[-110.8571443, 32.4586858]")

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        requestLocationUpdates()
    }

    fun isGooglePlayServicesAvailable(): Boolean {
        val status: Int =
            GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.requireContext())
        return if (ConnectionResult.SUCCESS == status) {
            true
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this.requireActivity(), 0).show()
            false
        }
    }


    private fun setNearByObservers(coordinate: String) {
        try {
            homeViewModel.nearByBar(coordinate)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<NearByBar> ->
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
                                    "Response" + resource.data?.let { response: Response<NearByBar> ->
                                        response.body().toString()
                                    })
                            }
                            com.peazyapp.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.peazyapp.peazy.utility.Status.LOADING -> {
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

    fun sendResponse(nearByBar: NearByBar) {
        try {
            progressDialog.dismiss()

            if (nearByBar.status == 200) {
                UserPreferenc.setStringPreference(
                    Constants.BAR_DETAIL_IMG_PATH,
                    "" + nearByBar.res.img_base_path
                )
                listbar = nearByBar.res.bar as ArrayList<Bar>
                homeViewModel.barlist.value = nearByBar.res.bar as ArrayList<Bar>
                Collections.sort(homeViewModel.barlist.value,
                    Comparator { c1, c2 -> c1.distance.compareTo(c2.distance) })
                if (listbar.size > 0)
                    mapFragment.getMapAsync(this)

            } else {
                val errors = nearByBar.err

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

    fun statusCheck(): Boolean {
        val manager: LocationManager =
            this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    private fun buildAlertMessageNoGps() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(this.requireActivity())
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
            .setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id: Int) {
                    dialog.cancel()
                }
            })
        val alert: android.app.AlertDialog? = builder.create()
        alert?.show()
    }


}

