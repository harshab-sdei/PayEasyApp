package com.example.peazy.controllers.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.peazy.R
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback, ConnectionCallbacks,
    OnConnectionFailedListener {
    var TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    var locationPermissionGranted: Boolean = false
    private val googleApiClient: GoogleApiClient? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null

    private var employeeLocation: LatLng? = null
    protected var locationManager: LocationManager? = null
    var isGPSEnabled = false
    var isNetworkEnabled = false
    private var permissionsList: ArrayList<String>? = null

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                employeeLocation = LatLng(location.latitude, location.longitude)
                //mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
                //mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
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
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?



        try {
            if (!checkLocationPermission()) {

                getPermissions()
            }
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this.requireActivity())
            mapFragment?.getMapAsync(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }


        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment


        // Specify the types of place data to return.
        activity?.let { Places.initialize(it, "mykey", Locale.US) };

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
        try {
            val myLocationButton: View = mapFragment!!.requireView().findViewById(0x2)
            // Specify the types of place data to return.

            if (myLocationButton != null && myLocationButton.layoutParams is RelativeLayout.LayoutParams) {
                // location button is inside of RelativeLayout
                val params: RelativeLayout.LayoutParams =
                    myLocationButton.layoutParams as RelativeLayout.LayoutParams

                // Align it to - parent BOTTOM|LEFT
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 50)
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 50)

                // Update margins, set to 10dp
                val margin = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ) as Int
                params.setMargins(margin, margin, margin, margin)

                myLocationButton.layoutParams = params
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        activity?.let { themeNavAndStatusBar(it) }
        return root
    }

    private fun checkLocationPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val res: Int = this.requireActivity().checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermissions() {
        val permissionsNeeded: MutableList<String> =
            ArrayList()
        Log.d("", "getPermissions: sms read and receive")
        permissionsList = ArrayList<String>()
        if (!addPermission(
                permissionsList!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) permissionsNeeded.add(Constants.ACCESS_FINE_LOCATION)
        if (permissionsList!!.size > 0) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), permissionsList!!.toTypedArray(),
                Constants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
            return
        }
        if (permissionsList!!.size == 0) {
//            redirect();
        }
    }

    private fun addPermission(
        permissionsList: MutableList<String>,
        permission: String
    ): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                permission
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            permissionsList.add(permission)
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this.requireActivity(),
                    permission
                )
            ) return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        val icon =
            BitmapDescriptorFactory.fromResource(R.drawable.pin)


        if (ContextCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true


        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-95.765325, 37.149371)
        val melbourne = mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Jamestown")
                .snippet("Jamestown, NY, the US")
                .icon(icon)
        )
        melbourne.setTag(0);

        val sydney1 = LatLng(-95.710351,37.055879)
        val melbourne1 = mMap.addMarker(
            MarkerOptions()
                .position(sydney1)
                .title("CH")
                .snippet("CHI, América 480, S2300 Rafaela, Santa Fe, Argentina")
                .icon(icon))
        melbourne1.setTag(0)

        val sydney2 = LatLng(-95.710013,37.056547)

        val melbourne2 = mMap.addMarker(
            MarkerOptions()
                .position(sydney2)
                .title("CH")
                .snippet("CHI, América 480, S2300 Rafaela, Santa Fe, Argentina")
                .icon(icon))
        melbourne2.setTag(0)

        val sydney3 = LatLng(-95.605308,37.029418)

        val melbourne3 = mMap.addMarker(
            MarkerOptions()
                .position(sydney3)
                .title("coffeyville")
                .snippet("coffeyville, S2300 Rafaela, Santa Fe, Argentina")
                .icon(icon))
        melbourne3.setTag(0)


        mMap.uiSettings.setMapToolbarEnabled(false)
        //    mMap.setOnMarkerClickListener(this)


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
                return
            }
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )

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
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        }
    }

    override fun onPause() {
        super.onPause()
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        checkAndRequestPermissions()
    }

    fun checkAndRequestPermissions(): Boolean {
        val permissionSendMessage = ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        return true
    }


}

