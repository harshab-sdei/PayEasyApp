package com.example.peazy.controllers

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.peazy.R
import com.example.peazy.models.logout.Logout
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.Resource
import com.example.peazy.utility.Status
import com.example.peazy.utility.appconfig.UserPreferenc
import com.example.peazy.webservices.RetrofitInsatance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.coroutines.Dispatchers
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var progressDialog: ProgressDialog
    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:Int=101
    var locationPermissionGranted:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val window: Window = this.window

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.setStatusBarColor(Color.TRANSPARENT)
        }
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_Profile

            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.logout.setOnClickListener {
            alertforLogout("Are You Sure, you want to logout?", "Confirm Alert")

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocationPermission()
        }
    }

    /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
         // Inflate the menu; this adds items to the action bar if it is present.
         menuInflater.inflate(R.menu.home, menu)
         return true
     }*/


        override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    fun alertforLogout(msg:String,title:String)
    {
        val dialogBuilder = AlertDialog.Builder(this@HomeActivity)

        // set message of alert dialog
        dialogBuilder.setMessage(msg)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
             .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                 dialog, which ->
                 setLogoutObservers()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(title)
        // show alert dialog
        alert.show()
    }

    fun logoutUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = RetrofitInsatance.apiService.logoutUser()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    private fun setLogoutObservers() {
        try {
            logoutUser()
                .observe(this, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {

                                resource.data?.let { response: Response<Logout> ->
                                    response.body().let { logout ->
                                        logout?.let { it1 ->
                                            sendResponse(it1)
                                        }
                                    }
                                }
                                /*  if(resource.data?.code()==404)
                                  {
                                      progressDialog!!.dismiss()

                                      Toast.makeText(this,"Internal Server Error",Toast.LENGTH_LONG).show()

                                  }*/
                                Log.d(
                                    "TAG",
                                    "Response" + resource.data?.let { response: Response<Logout> ->
                                        response.body().toString()
                                    })
                            }
                            Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e("TAG", "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e("TAG", e.message)
                                }

                            }
                            Status.LOADING -> {
                                progressDialog = ProgressDialog(this@HomeActivity)

                                progressDialog!!.setMessage("loading...")
                                progressDialog!!.show()


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e("TAG", e.message)
        }
    }
    fun sendResponse(signUP: Logout) {
        try {
            progressDialog!!.dismiss()

            if (signUP.status == 200) {
                    UserPreferenc.setBooleanPreference(Constants.IS_USER_Login,false)
                    finish()

            } else {
                val errors = signUP.err
                if (errors.errCode == 5) {
                    AppUtility.getInstance().alertDialogWithSingleButton(
                        applicationContext,
                        "Error",
                        "Error in  Sign Up"
                    )
                }else {
                    AppUtility.getInstance()
                        .alertDialogWithSingleButton(
                            this@HomeActivity,
                            "Error",
                            "" + errors.msg
                        )
                }
            }


        } catch (e: Exception) {
            Log.e("TAG", e.message)
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
    }


}