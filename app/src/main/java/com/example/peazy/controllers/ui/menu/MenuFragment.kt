package com.example.peazy.controllers.ui.menu

import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peazy.R
import com.example.peazy.controllers.ui.menu.barmenuitemadepter.MenuItemAdepter
import com.example.peazy.controllers.ui.menu.catagory_adepter.MenuAdapter
import com.example.peazy.controllers.ui.menu.subcategory_adepter.SubCategoryAdepter
import com.example.peazy.models.category.Category
import com.example.peazy.models.category.MenuCategory
import com.example.peazy.models.menu_item.BarMenuItem
import com.example.peazy.models.menu_item.Item
import com.example.peazy.models.subcategory.SubCategory
import com.example.peazy.models.subcategory.SubcategoryX
import com.example.peazy.utility.AppUtility
import com.example.peazy.utility.Constants
import com.example.peazy.utility.appconfig.UserPreferenc
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.main_fragment2.view.*
import retrofit2.Response
import java.util.*


class MenuFragment : Fragment() {

    companion object {
        fun newInstance() = MenuFragment()
        var itemCount: Int = 0
        var price_total: Int = 0
    }

    var listcat = ArrayList<Category>()
    var listsubcat = ArrayList<SubcategoryX>()
    var bar_menu_item = ArrayList<Item>()
    var catid: String? = null
    var subcatid: String? = null
    lateinit var root: View
    lateinit var progressDialog: ProgressDialog
    private lateinit var viewModel: MainViewModel
    var TAG: String = "MenuFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.main_fragment2, container, false)
        return root
    }

    var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        var params = mapOf("sort" to "1")
        setMenuCategoryObservers(params)
        sheetBehavior = BottomSheetBehavior.from(root.bottom_sheet)
        root.myrecleview.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        root.recleview_subcat.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var layoutManager: GridLayoutManager? = null
        if (getResources()
                .getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT
        ) {
            layoutManager = GridLayoutManager(this.requireContext(), 3)
        } else {
            layoutManager = GridLayoutManager(this.requireContext(), 7)
        }
        root.recleview_menuitem!!.setLayoutManager(layoutManager)


        sheetBehavior!!.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> sheetBehavior!!.setHideable(
                        false
                    )
                }
            }

            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
            }
        })
        sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

    }

    fun listItemClick(category: Category) {
        catid = category.cat_id
        var fullUrl = "api/v2/subcategory/" + category.cat_id + "/list"
        var params = mapOf("sort" to "1")
        Log.e(TAG, "Full URL=" + fullUrl)
        setSubCategoryObservers(fullUrl, params)
        // Toast.makeText(this@,fruitList.fruitname, Toast.LENGTH_LONG).show()
    }

    fun subCat_listItemClick(subCategory: SubcategoryX) {
        subcatid = subCategory.subcat_id

        var params = mapOf(
            "bar_id" to "5f18848084a54e45dc164022",
            "cat_id" to "" + catid,
            "subcat_id" to "" + subcatid,
            "sort" to "1"
        )
        println(params)

        setMenuItemObservers(params)
    }

    fun barMenulistItemClick(item: Item) {
        itemCount++

        root.item_total.text = "" + itemCount + " Item"
        price_total += item.price
        root.total_price.text = "" + price_total

        sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED


    }

    private fun setMenuCategoryObservers(params: Map<String, String>) {
        try {
            viewModel.getCategoryList(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<MenuCategory> ->
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
                                    "Response" + resource.data?.let { response: Response<MenuCategory> ->
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

    fun sendResponse(menuCategory: MenuCategory) {
        try {
            progressDialog!!.dismiss()

            if (menuCategory.status == 200) {
                UserPreferenc.setStringPreference(
                    Constants.MENU_CAT_IMG_PATH,
                    "" + menuCategory.res.image_base_path
                )

                listcat = menuCategory.res.category as ArrayList<Category>
                root.myrecleview.adapter =
                    MenuAdapter(
                        listcat,
                        { selsectItem: Category -> listItemClick(selsectItem) })
                try {
                    var fullUrl = "api/v2/subcategory/" + listcat.get(0).cat_id + "/list"
                    catid = listcat.get(0).cat_id
                    var params = mapOf("sort" to "1")
                    Log.e(TAG, "Full URL=" + fullUrl)
                    setSubCategoryObservers(fullUrl, params)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                AppUtility.getInstance().alertDialogWithSingleButton(
                    this.requireContext(),
                    "Error",
                    "Somthing Wrong"
                )
            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun setSubCategoryObservers(fullUrl: String, params: Map<String, String>) {
        try {
            viewModel.getSubCategory(fullUrl, params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<SubCategory> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponseOfSubcategory(
                                                it1
                                            )
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<SubCategory> ->
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

    fun sendResponseOfSubcategory(subCategory: SubCategory) {
        try {
            progressDialog!!.dismiss()

            if (subCategory.status == 200) {
                //    UserPreferenc.setStringPreference(Constants.MENU_CAT_IMG_PATH,""+menuCategory.res.image_base_path)
                listsubcat = subCategory.res.subcategory as ArrayList<SubcategoryX>
                root.recleview_subcat.adapter =
                    SubCategoryAdepter(
                        listsubcat,
                        { selsectItem: SubcategoryX -> subCat_listItemClick(selsectItem) })

                subcatid = subCategory.res.subcategory.get(0).subcat_id

                var params = mapOf(
                    "bar_id" to "5f18848084a54e45dc164022",
                    "cat_id" to "" + catid,
                    "subcat_id" to "" + subcatid,
                    "sort" to "1"
                )

                setMenuItemObservers(params)
            } else {
                AppUtility.getInstance().alertDialogWithSingleButton(
                    this.requireContext(),
                    "Error",
                    "Somthing Wrong"
                )
            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun setMenuItemObservers(params: Map<String, String>) {
        try {
            viewModel.getMenuItem(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.example.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<BarMenuItem> ->
                                    response.body().let { signUP ->
                                        signUP?.let { it1 ->
                                            sendResponseOfMenuItem(
                                                it1
                                            )
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<BarMenuItem> ->
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

    fun sendResponseOfMenuItem(barMenuItem: BarMenuItem) {
        try {
            progressDialog!!.dismiss()

            if (barMenuItem.status == 200) {
                UserPreferenc.setStringPreference(
                    Constants.MENU_ITEM_PATH,
                    "" + barMenuItem.res.image_base_path
                )
                bar_menu_item = barMenuItem.res.item as ArrayList<Item>
                root.recleview_menuitem.adapter =
                    MenuItemAdepter(
                        bar_menu_item,
                        { selsectItem: Item -> barMenulistItemClick(selsectItem) })

            } else {

                AppUtility.getInstance().alertDialogWithSingleButton(
                    this.requireContext(),
                    "Error",
                    "Somthing Wrong"
                )


            }


        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }


}