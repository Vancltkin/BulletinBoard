package com.vanclykin.bulletinboard.fragment

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.databinding.ImageListFragBinding
import com.vanclykin.bulletinboard.dialoghelper.ProgressDialog
import com.vanclykin.bulletinboard.utils.AdapterCallback
import com.vanclykin.bulletinboard.utils.ImageManager
import com.vanclykin.bulletinboard.utils.ImagePicker
import com.vanclykin.bulletinboard.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFrag(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<String>?) : Fragment(), AdapterCallback {

    lateinit var binding: ImageListFragBinding
    val adapter = SelectImageRvAdapter(this)
    private val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var addImageItem: MenuItem? = null
    private var job: Job? = null // job для корутин

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = ImageListFragBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        touchHelper.attachToRecyclerView(binding.rcViewSelectImage)
        binding.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        binding.rcViewSelectImage.adapter = adapter
        if (newList != null) {
            resizeSelectedImages(newList, true)
        }
    }

    override fun onItemDelete() {
        addImageItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>){
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() { //закрытие фрагмента

        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job?.cancel() // завершить корутину
    }

    private fun resizeSelectedImages(newList: ArrayList<String>, needClear: Boolean){

        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity as Activity) // добавление progressBar
            val bitmapList = ImageManager.imageResize(newList)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if (adapter.mainArray.size > 2) addImageItem?.isVisible = false
        }
    }

    private fun setUpToolbar(){

        binding.toolbar.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = binding.toolbar.menu.findItem(R.id.id_delete_image)
        addImageItem = binding.toolbar.menu.findItem(R.id.id_add_image)

        binding.toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(),true)
            addImageItem?.isVisible = true
            true
        }

        addImageItem?.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity,imageCount,ImagePicker.REQUEST_CODE_GET_IMAGES)
            true
        }
    }

    fun updateAdapter(newList: ArrayList<String>) {

        resizeSelectedImages(newList,false)
    }

    fun  setSingleImage (uri: String, pos: Int){
        val pBar = binding.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri))
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos)
        }

    }


}