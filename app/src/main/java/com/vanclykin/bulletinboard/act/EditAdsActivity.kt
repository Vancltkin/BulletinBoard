package com.vanclykin.bulletinboard.act

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.adapters.ImageAdapter
import com.vanclykin.bulletinboard.databinding.ActivityEditAdsBinding
import com.vanclykin.bulletinboard.dialogs.DialogSpinnerHelper
import com.vanclykin.bulletinboard.fragment.FragmentCloseInterface
import com.vanclykin.bulletinboard.fragment.ImageListFrag
import com.vanclykin.bulletinboard.utils.CityHelper
import com.vanclykin.bulletinboard.utils.ImagePicker

class EditAdsActivity : AppCompatActivity(), FragmentCloseInterface {
    private var chooseImageFrag: ImageListFrag? = null
    lateinit var binding: ActivityEditAdsBinding
    private var dialog = DialogSpinnerHelper()
    private lateinit var imageAdapter: ImageAdapter
    var editImagePos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {

            if (data != null) {

                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                if (returnValues?.size!! > 1 && chooseImageFrag == null) {

                    openChooseImageFrag(returnValues)

                } else if (returnValues.size == 1 && chooseImageFrag == null) {

                    imageAdapter.update(returnValues)

                } else if (chooseImageFrag != null) {

                    chooseImageFrag?.updateAdapter(returnValues)

                }
            }

        } else if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGES){

            if (data != null) {

                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                chooseImageFrag?.setSingleImage(uris?.get(0)!!,editImagePos)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES)

                } else {

                    Toast.makeText(
                        this@EditAdsActivity,
                        "Approve permissions to open Pix",
                        Toast.LENGTH_LONG
                    ).show()
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun init() {
        imageAdapter = ImageAdapter()
        binding.vpImages.adapter = imageAdapter

    }

    //onClick
    fun onClickSelectCounty(view: View) {

        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, binding.tvCountry)
        if (binding.tvCity.text.toString() != getString(R.string.select_city)) {
            binding.tvCity.text = getString(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View) {

        val selectedCountry = binding.tvCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)) {
            val listCity = CityHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity, binding.tvCity)

        } else {
            Toast.makeText(this, R.string.no_result, Toast.LENGTH_LONG).show()
        }
    }

    fun onClickGetImages(view: View) {

        if (imageAdapter.mainArray.size == 0) {
            ImagePicker.getImages(this, 3,ImagePicker.REQUEST_CODE_GET_IMAGES)

        } else {

            openChooseImageFrag(imageAdapter.mainArray)
        }

    }

    override fun onFragClose(list: ArrayList<String>) {

        binding.ScrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    private fun  openChooseImageFrag (newList: ArrayList<String>){

        chooseImageFrag = ImageListFrag(this, newList)
        binding.ScrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.placeHolder, chooseImageFrag!!)
        fm.commit()
    }

}