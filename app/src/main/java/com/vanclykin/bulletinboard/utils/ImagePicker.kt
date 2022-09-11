package com.vanclykin.bulletinboard.utils

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.vanclykin.bulletinboard.act.EditAdsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImagePicker {
    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGES = 1000

    fun getImages(context: AppCompatActivity, imageCounter: Int, rCode: Int) {
        val options = Options.init()
            .setRequestCode(rCode)
            .setCount(imageCounter)
            .setFrontfacing(false)
            .setMode(Options.Mode.Picture)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/pix/images")

        Pix.start(context,options)
    }

    fun showSelectedImages(resultCode: Int,requestCode: Int, data: Intent?, editAdsActivity: EditAdsActivity){
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {

            if (data != null) {

                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                if (returnValues?.size!! > 1 && editAdsActivity.chooseImageFrag == null) {

                    editAdsActivity.openChooseImageFrag(returnValues)

                } else if (editAdsActivity.chooseImageFrag != null) {

                    editAdsActivity.chooseImageFrag?.updateAdapter(returnValues)

                } else if (returnValues.size == 1 && editAdsActivity.chooseImageFrag == null) {

                    CoroutineScope(Dispatchers.Main).launch {

                        editAdsActivity.binding.pBarLoad.visibility = View.VISIBLE
                        val bitMapArray = ImageManager.imageResize(returnValues) as ArrayList <Bitmap>
                        editAdsActivity.binding.pBarLoad.visibility = View.GONE
                        editAdsActivity.imageAdapter.update(bitMapArray)
                    }
                }
            }

        } else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_SINGLE_IMAGES){

            if (data != null) {

                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                editAdsActivity.chooseImageFrag?.setSingleImage(uris?.get(0)!!,editAdsActivity.editImagePos)
            }
        }
    }
}