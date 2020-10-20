package com.peazyapp.peazy.controllers.ui.menu

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.peazyapp.peazy.R
import com.peazyapp.peazy.controllers.ui.menu.catagory_adepter.MenuAdapter
import com.peazyapp.peazy.controllers.ui.menu.sub_sub_category.SubSubCategoryAdepter
import com.peazyapp.peazy.controllers.ui.menu.subcategory_adepter.SubCategoryAdepter
import com.peazyapp.peazy.models.category.Category
import com.peazyapp.peazy.models.category.MenuCategory
import com.peazyapp.peazy.models.menu_item.Item
import com.peazyapp.peazy.models.menu_item.MenuItems
import com.peazyapp.peazy.models.subcategory.SubCategory
import com.peazyapp.peazy.models.subcategory.SubcategoryX
import com.peazyapp.peazy.models.subsubcategory.SubItem
import com.peazyapp.peazy.models.subsubcategory.SubSubCategory
import com.peazyapp.peazy.models.subsubcategory.Subcategory
import com.peazyapp.peazy.utility.AppUtility
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.main_fragment2.view.*
import retrofit2.Response


class MenuFragment : Fragment() {

    companion object {
        fun newInstance() = MenuFragment()
        var itemCount: Int = 0
        var price_total: Double = 0.0
        lateinit var root: View
        var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null


    }

    var item1 = ArrayList<Item>()
    var listcat = ArrayList<Category>()
    var listsubcat = ArrayList<SubcategoryX>()
    var listsubsubcat = ArrayList<Subcategory>()
    var catid: String? = null
    var subcatid: String? = null
    lateinit var progressDialog: ProgressDialog
    private lateinit var viewModel: MainViewModel
    var TAG: String = "MenuFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.main_fragment2, container, false)
        try {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        } catch (e: Exception) {
        }
        return root
    }


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
        root.recleview_subsubcat.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.itemCount.observe(this.requireActivity(), Observer {
            updateOder()
        })

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

        try {
            root.item_total.text = "" + itemCount + " Item"
            root.total_price.text = "" + String.format("%.2f", price_total)
        } catch (e: java.lang.Exception) {
        }
        sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        root.bt_add_in_cart.setOnClickListener {

            var bundle = Bundle()
            bundle.putString("bar_id", Constants.bar_id)
            findNavController().navigate(R.id.action_menuFragment_to_addCartFragment, bundle)
        }
    }

    fun listItemClick(category: Category) {
        catid = category.cat_id
        var fullUrl = "api/v2/subcategory/" + category.cat_id + "/list"
        var params = mapOf("sort" to "1")
        Log.e(TAG, "Full URL=" + fullUrl)
        setSubCategoryObservers(fullUrl, params)
    }

    fun subCat_listItemClick(subCategory: SubcategoryX) {
        subcatid = subCategory.subcat_id


        var params1 = mapOf(
            "bar_id" to "" + Constants.bar_id,
            "cat_id" to "" + catid,
            "sub_cat_id" to "" + subcatid,
            "sort" to "1"
        )
        setMenuItemObservers(params1)
        println(params1)


    }

    /*  fun inititemCount()
      {
          viewModel.itemCount.value= itemCount
      }*/
    fun subSubCat_listItemClick(subSubCategory: Subcategory) {
        subcatid = subSubCategory.subcat_id

    }

    fun barMenulistItemClick(item: Item) {
        /*var isinsert = false

        for (additem in Constants.addcartlist) {

            try {
                if (additem.item_id == item.item_id && item.price > 0) {
                    additem.num_of_unit++
                    isinsert = true
                }

            } catch (e: java.lang.Exception) {
            }
        }
        if (!isinsert) {
            var addItem = Add_Item(
                item.image,
                item.item_id,
                item.name,
                item.price,
                item.num_of_unit
            )
            Constants.addcartlist.add(addItem)
        }*/


        print("add Item size" + Constants.addcartlist.size)
        updateOder()

    }

    override fun onResume() {
        super.onResume()
        try {
            root.item_total.text = "" + itemCount + " Item"
            root.total_price.text = "" + String.format("%.2f", price_total)
        } catch (e: java.lang.Exception) {
        }
    }

    private fun setMenuCategoryObservers(params: Map<String, String>) {
        try {
            viewModel.getCategoryList(params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

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

    fun sendResponse(menuCategory: MenuCategory) {
        try {
            progressDialog.dismiss()

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
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

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

    fun sendResponseOfSubcategory(subCategory: SubCategory) {
        try {
            progressDialog.dismiss()

            if (subCategory.status == 200) {
                listsubcat = subCategory.res.subcategory as ArrayList<SubcategoryX>
                root.recleview_subcat.adapter =
                    SubCategoryAdepter(
                        listsubcat,
                        { selsectItem: SubcategoryX -> subCat_listItemClick(selsectItem) })

                subcatid = subCategory.res.subcategory.get(0).subcat_id

                var params = mapOf(
                    "bar_id" to "" + Constants.bar_id,
                    "cat_id" to "" + catid,
                    "sub_cat_id" to "" + subcatid,
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


    private fun setSubSubCategoryObservers(fullUrl: String, params: Map<String, String>) {
        try {
            viewModel.getSubSubCategory(fullUrl, params)
                .observe(this.requireActivity(), androidx.lifecycle.Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<SubSubCategory> ->
                                    response.body().let { subcat ->
                                        subcat?.let { it1 ->
                                            sendResponseOfSubSubcategory(
                                                it1
                                            )
                                        }
                                    }
                                }
                                Log.d(
                                    TAG,
                                    "Response" + resource.data?.let { response: Response<SubSubCategory> ->
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

    fun sendResponseOfSubSubcategory(subSubCategory: SubSubCategory) {
        try {
            progressDialog!!.dismiss()

            if (subSubCategory.status == 200) {
                listsubsubcat = subSubCategory.res.subcategory as ArrayList<Subcategory>

                try {
                    var i: Int = 0
                    if (item1.size > 0) {
                        while (i < listsubsubcat.size) {

                            var list1 = ArrayList<SubItem>()
                            var j = 0
                            while (j < item1.size) {
                                if (item1.get(j).sub_sub_cat.equals(listsubsubcat.get(i).name)) {
                                    list1.add(
                                        SubItem(
                                            item1.get(j).description,
                                            item1.get(j).image,
                                            item1.get(j).item_id,
                                            item1.get(j).name,
                                            item1.get(j).price,
                                            item1.get(j).sub_sub_cat,
                                            item1.get(j).sub_sub_cat_id,
                                            0
                                        )
                                    )
                                }
                                j++
                            }
                            listsubsubcat.get(i).subItem = list1 as ArrayList<SubItem>

                            i++


                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                root.recleview_subsubcat.adapter =
                    SubSubCategoryAdepter(requireContext(),
                        listsubsubcat,
                        { selsectItem: Subcategory -> subSubCat_listItemClick(selsectItem) })

                subcatid = subSubCategory.res.subcategory.get(0).subcat_id

                /* var params = mapOf(
                     "bar_id" to "" + Constants.bar_id,
                     "cat_id" to "" + catid,
                     "subcat_id" to "" + subcatid,
                     "sort" to "1"
                 )

                 setMenuItemObservers(params)*/
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
                            com.peazyapp.peazy.utility.Status.SUCCESS -> {

                                resource.data?.let { response: Response<MenuItems> ->
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
                                    "Response" + resource.data?.let { response: Response<MenuItems> ->
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


    fun sendResponseOfMenuItem(barMenuItem: MenuItems) {
        try {
            progressDialog.dismiss()

            if (barMenuItem.status == 200) {
                UserPreferenc.setStringPreference(
                    Constants.MENU_ITEM_PATH,
                    "" + barMenuItem.res.image_base_path
                )

                item1 = barMenuItem.res.item as ArrayList<Item>

                var fullUrl = "api/v2/subsubcategory/" + catid + "/" + subcatid + "/list"
                var params = mapOf("sort" to "1")
                Log.e(TAG, "Full URL=" + fullUrl)
                setSubSubCategoryObservers(fullUrl, params)

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

    fun updateOder() {
        root.item_total.text = "" + itemCount + " Item"
        root.total_price.text = Constants.currency + String.format("%.2f", price_total)
        sheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

}