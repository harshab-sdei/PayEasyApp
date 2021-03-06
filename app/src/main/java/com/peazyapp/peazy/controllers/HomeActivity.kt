package com.peazyapp.peazy.controllers

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.amulyakhare.textdrawable.TextDrawable
import com.peazyapp.peazy.MainActivity
import com.peazyapp.peazy.R
import com.peazyapp.peazy.models.logout.Logout
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.Resource
import com.peazyapp.peazy.utility.Status
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import com.peazyapp.peazy.webservices.RetrofitInsatance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.peazyapp.peazy.controllers.ui.menu.Menu
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
        val txtusernm: TextView = navView.getHeaderView(0).findViewById(R.id.usernm)
        val txtuseremail: TextView = navView.getHeaderView(0).findViewById(R.id.useremail)
        val imghome_profilepic: ImageView =
            navView.getHeaderView(0).findViewById(R.id.home_profilepic)
        txtusernm.text = UserPreferenc.getStringPreference(Constants.USER_NAME, "")
        txtuseremail.text = UserPreferenc.getStringPreference(Constants.USER_EMAIL, "")
        try {

            Log.d(
                "Device Token",
                "" + UserPreferenc.getStringPreference(
                    Constants.DEVICE_TOKEN,
                    ""
                ) + "   accessToken=" + UserPreferenc.getStringPreference(
                    Constants.ACCESS_TOKEN,
                    ""
                )
            )
            val drawable = TextDrawable.builder()
                .buildRound(
                    AppUtility.getInstance().getFirstandLast(txtusernm.text.toString()),
                    R.color.White
                )

            imghome_profilepic.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocationPermission()
        }

        navView.setNavigationItemSelectedListener {
            navController.currentDestination!!.id

            val id: Int = it.getItemId()

            if (id == R.id.nav_logout) {
                alertforLogout("Are You Sure, you want to logout?", "Confirm Alert")
            }
            if (id == R.id.nav_home) {
                navController.navigate(id)
            }
            if (id == R.id.nav_Profile) {
                navController.navigate(id)
            }
            if (id == R.id.nav_order_history) {
                navController.navigate(id)
            }

            drawerLayout.closeDrawers()

            true
        }
        /* setupActionBarWithNavController( navController, drawerLayout)
         navView.setupWithNavController(navController)*/



    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun alertforLogout(msg: String, title: String) {
        val dialogBuilder = AlertDialog.Builder(this@HomeActivity)

        // set message of alert dialog
        dialogBuilder.setMessage(msg)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { _, _ ->
                setLogoutObservers()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
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
                                    progressDialog.dismiss()
                                    Log.e("TAG", "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e("TAG", e.message)
                                }

                            }
                            Status.LOADING -> {
                                progressDialog = ProgressDialog(this@HomeActivity)

                                progressDialog.setMessage("loading...")
                                progressDialog.show()


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
            progressDialog.dismiss()

            if (signUP.status == 200) {
                UserPreferenc.setBooleanPreference(Constants.IS_USER_Login, false)
                UserPreferenc.setBooleanPreference(Constants.IS_USER_Choose_Mode, false)
                val homeIntent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(homeIntent)
                finish()

            } else {
                val errors = signUP.err
                if (errors.errCode == 1) {
                    AppUtility.getInstance().alertDialogWithSingleButton(
                        applicationContext,
                        "Error",
                        "" + errors.msg
                    )
                } else {
                    UserPreferenc.setBooleanPreference(Constants.IS_USER_Login, false)
                    UserPreferenc.setBooleanPreference(Constants.IS_USER_Choose_Mode, false)
                    val homeIntent = Intent(this@HomeActivity, MainActivity::class.java)
                    startActivity(homeIntent)
                    finish()
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