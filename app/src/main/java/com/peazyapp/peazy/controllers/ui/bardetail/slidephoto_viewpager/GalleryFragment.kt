package com.peazyapp.peazy.controllers.ui.bardetail.slidephoto_viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.peazyapp.peazy.R
import com.peazyapp.peazy.utility.Constants
import com.peazyapp.peazy.utility.appconfig.UserPreferenc
import kotlinx.android.synthetic.main.fragment_gallery.view.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        var index = requireArguments().getString(EXTRA_ITEM_INDEX)
        root.btnPrevious.alpha = 1f
        root.btnPrevious.alpha = 1f
        index = UserPreferenc.getStringPreference(Constants.BAR_DETAIL_IMG_PATH, "") + index

        if (index !== null) {
            Glide.with(this)
                .load(index)
                .into(root.img)
        } else {
            root.img.setImageResource(R.drawable.temp_detail_img)
        }


        return root
    }


    companion object {
        val EXTRA_ITEM_INDEX = "images"

        fun newInstance(image: String): GalleryFragment {
            val f = GalleryFragment()
            val bdl = Bundle()
            bdl.putString(EXTRA_ITEM_INDEX, image)
            f.arguments = bdl

            return f
        }
    }
}