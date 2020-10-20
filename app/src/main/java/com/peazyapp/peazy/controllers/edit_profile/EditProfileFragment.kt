package com.peazyapp.peazy.controllers.edit_profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amulyakhare.textdrawable.TextDrawable
import com.peazyapp.peazy.MainActivity
import com.peazyapp.peazy.R
import com.peazyapp.peazy.databinding.EditProfileFragmentBinding
import com.peazyapp.peazy.models.changepws.ChangePWS
import com.peazyapp.peazy.models.editprofile.EditProfile
import com.peazyapp.peazy.models.logout.Logout
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.Status
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.change_password.view.*
import kotlinx.android.synthetic.main.dialog_editname.view.*
import retrofit2.Response


class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    var name: String? = ""
    var TAG = "EditProfileFragment"
    private lateinit var viewModel: EditProfileViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var databinding: EditProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding =
            DataBindingUtil.inflate(inflater, R.layout.edit_profile_fragment, container, false)

        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)

        databinding.pfName.text = UserPreferenc.getStringPreference(Constants.USER_NAME, "")
        databinding.pfEmail.text = UserPreferenc.getStringPreference(Constants.USER_EMAIL, "")
        try {
            val drawable = TextDrawable.builder()
                .buildRoundRect(
                    AppUtility.getInstance().getFirstandLast(databinding.pfName.text.toString()),
                    R.color.orange,
                    12
                )

            databinding.profilepic.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        databinding.btChangepws.setOnClickListener {
            showDialog()
        }

        databinding.btEditprofile.setOnClickListener {
            showDialogname()
        }


        /*databinding.btLogin.setOnClickListener {
            if (AppUtility.getInstance()
                    .validateLetters(databinding.editTextname.text.toString()) &&
                AppUtility.getInstance()
                    .isPasswordValid(databinding.editTextpassword.text.toString())
            ) {

                val params = mapOf(
                    "name" to databinding.editTextname.text.toString(),
                    "password" to databinding.editTextpassword.text.toString()
                )
                setObservers(params)

            } else {
                if (!AppUtility.getInstance()
                        .validateLetters(databinding.editTextname.text.toString())
                ) {
                    databinding.editTextname.setError(Constants.email_error)
                } else {
                    databinding.editTextname.setError(null)
                }

                if (!AppUtility.getInstance()
                        .isPasswordValid(databinding.editTextpassword.text.toString())
                ) {
                    databinding.editTextpassword.setError(Constants.pws_error)
                } else {
                    databinding.editTextpassword.setError(null)

                }
            }

        }*/

        databinding.btLogout.setOnClickListener {
            setLogoutObservers()
        }
    }


    private fun setObservers(params: Map<String, String>) {
        try {
            viewModel.editProfile(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<EditProfile> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponse(it1)
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<EditProfile> ->
                                        response.body().toString()
                                    })
                            }
                            com.peazyapp.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.peazyapp.peazy.utility.Status.LOADING -> {
                                progressDialog = ProgressDialog(this.requireContext())

                                progressDialog.setMessage("loading...")
                                progressDialog.show()


                            }
                        }
                    }
                })
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    fun sendResponse(editProfile: EditProfile) {
        try {
            progressDialog!!.dismiss()

            if (editProfile.status == 200) {
                if (editProfile.res.is_deleted == 1) {
                    AppUtility.getInstance()
                        .alertDialogWithSingleButton(
                            this.requireContext(),
                            "Alert",
                            "User is already deleted"
                        )
                } else {
                    AppUtility.getInstance()
                        .alertDialogWithSingleButton(
                            this.requireContext(),
                            "Alert",
                            "Successfully Save to changes"
                        )
                    UserPreferenc.save(Constants.USER_NAME, "" + name)
                    databinding.pfName.text =
                        UserPreferenc.getStringPreference(Constants.USER_NAME, "")
                    try {
                        val drawable = TextDrawable.builder()
                            .buildRoundRect(
                                AppUtility.getInstance()
                                    .getFirstandLast(databinding.pfName.text.toString()),
                                R.color.orange,
                                12
                            )

                        databinding.profilepic.setImageDrawable(drawable)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            } else {
                val errors = editProfile.err

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

    private fun setLogoutObservers() {
        try {
            viewModel.logoutUser()
                .observe(this.requireActivity(), Observer {
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
                                progressDialog = ProgressDialog(this.requireContext())

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

                val homeIntent = Intent(this.requireActivity(), MainActivity::class.java)
                startActivity(homeIntent)
                requireActivity().finish()


            } else {
                val errors = signUP.err
                if (errors.errCode == 5) {
                    AppUtility.getInstance().alertDialogWithSingleButton(
                        this.requireActivity(),
                        "Error",
                        "Error in  Sign Up"
                    )
                } else {
                    AppUtility.getInstance()
                        .alertDialogWithSingleButton(
                            this.requireActivity(),
                            "Error",
                            "" + errors.msg
                        )
                }
            }


        } catch (e: Exception) {
            Log.e("TAG", e.message)
        }
    }


    fun showDialog() {
        val mDialogView =
            LayoutInflater.from(this.requireContext())
                .inflate(R.layout.change_password, null)
        val mBuilder = AlertDialog.Builder(this.requireContext())
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.close.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mDialogView.bt_changepws.setOnClickListener {
            if (mDialogView.edold_pass.text.toString().isEmpty()) {
                mDialogView.edold_pass.error = "Password is not empty"
            }
            if (mDialogView.editpassword.text.toString().isEmpty()) {
                mDialogView.editpassword.error = "Password is not empty"
            } else {
                val params = mapOf(
                    "old_pass" to mDialogView.edold_pass.text.toString(),
                    "new_pass" to mDialogView.editpassword.text.toString()
                )
                setChangePasswordObservers(params)
            }
        }
    }

    fun showDialogname() {
        val mDialogView =
            LayoutInflater.from(this.requireContext())
                .inflate(R.layout.dialog_editname, null)
        val mBuilder = AlertDialog.Builder(this.requireContext())
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.in_close.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mDialogView.bt_edit.setOnClickListener {
            if (mDialogView.ed_user.text.toString().isEmpty()) {
                mDialogView.ed_user.error = "User Name is not empty"
            }
            name = mDialogView.ed_user.text.toString()
            val params = mapOf(
                "name" to mDialogView.ed_user.text.toString()
            )
            setObservers(params)
            mAlertDialog.dismiss()

        }

    }


    private fun setChangePasswordObservers(params: Map<String, String>) {
        try {
            viewModel.changePassword(params)
                .observe(this.requireActivity(), Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {

                                resource.data?.let { response: Response<ChangePWS> ->
                                    response.body().let { logout ->
                                        logout?.let { it1 ->
                                            sendChangePwsResponse(it1)
                                        }
                                    }
                                }

                                Log.d(
                                    "TAG",
                                    "Response" + resource.data?.let { response: Response<ChangePWS> ->
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
                                progressDialog = ProgressDialog(this.requireContext())

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

    fun sendChangePwsResponse(changePWS: ChangePWS) {
        try {
            progressDialog.dismiss()

            if (changePWS.status == 200) {


            } else {
                val errors = changePWS.err
                if (errors.errCode == 5) {
                    AppUtility.getInstance().alertDialogWithSingleButton(
                        this.requireActivity(),
                        "Error",
                        "Error in  change Password"
                    )
                } else {
                    AppUtility.getInstance()
                        .alertDialogWithSingleButton(
                            this.requireActivity(),
                            "Error",
                            "" + errors.msg
                        )
                }
            }


        } catch (e: Exception) {
            Log.e("TAG", e.message)
        }
    }
}