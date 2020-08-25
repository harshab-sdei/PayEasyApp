package com.example.peazy.controllers.edit_profile

import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.peazy.R
import com.example.peazy.databinding.EditProfileFragmentBinding
import com.example.peazy.databinding.MainFragment3Binding
import com.example.peazy.models.addpaycard.AddPayCard
import com.example.peazy.models.editprofile.EditProfile
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import retrofit2.Response

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

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

        databinding.btLogin.setOnClickListener {
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

        }
    }


    private fun setObservers(params: Map<String, String>) {
        try {
            viewModel.editProfile(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

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
                            com.example.peazy.utility.Status.ERROR -> {
                                try {
                                    progressDialog!!.dismiss()
                                    Log.e(TAG, "" + resource.message)
                                } catch (e: Exception) {
                                    Log.e(TAG, e.message)
                                }

                            }
                            com.example.peazy.utility.Status.LOADING -> {
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
}