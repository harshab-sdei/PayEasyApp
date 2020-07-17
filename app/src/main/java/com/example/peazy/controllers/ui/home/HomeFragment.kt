package com.example.peazy.controllers.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.peazy.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places.getGeoDataClient
import com.google.android.gms.location.places.Places.getPlaceDetectionClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
var TAG="HomeFragment"
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    var locationPermissionGranted=false
    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=101
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
        mapFragment?.getMapAsync(this)
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
        activity?.let { themeNavAndStatusBar(it) }
        return root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        val icon =
            BitmapDescriptorFactory.fromResource(R.drawable.pin)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-95.765325, 37.149371)
        val melbourne = mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Jamestown")
                .snippet("Jamestown, NY, the US")
                .icon(icon))
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

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        mMap.uiSettings.setMapToolbarEnabled(false)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 08f))
        mMap.setOnMarkerClickListener(this);


    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        // Retrieve the data from the marker.

        // Retrieve the data from the marker.
        var clickCount = marker!!.getTag()

        // Check if a click count was set, then display the click count.

        // Check if a click count was set, then display the click count.
      /*  if (clickCount != null) {
            clickCount = clickCount + 1
            marker.setTag(clickCount)
            Toast.makeText(
                this,
                marker.getTitle().toString() +
                        " has been clicked " + clickCount + " times.",
                Toast.LENGTH_SHORT
            ).show()
        }*/
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
         locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                mMap?.isMyLocationEnabled = false
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
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


}