package com.vanclykin.bulletinboard.fragment

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.act.EditAdsActivity
import com.vanclykin.bulletinboard.databinding.SelectImageFragItemBinding
import com.vanclykin.bulletinboard.utils.AdapterCallback
import com.vanclykin.bulletinboard.utils.ImageManager
import com.vanclykin.bulletinboard.utils.ImagePicker
import com.vanclykin.bulletinboard.utils.ItemTouchMoveCallback

class SelectImageRvAdapter(val adapterCallback: AdapterCallback) : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {

        val viewBinding = SelectImageFragItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(viewBinding, parent.context, this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) { // перенос в массиве

        val targetItem = mainArray[targetPos] //сохранение элемента
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPos)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(val viewBinding: SelectImageFragItemBinding, var context: Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(viewBinding.root) {

        fun setData(bitmap: Bitmap) {

            viewBinding.imEditImage.setOnClickListener{

                ImagePicker.getImages(context as EditAdsActivity, 1, ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGES)
                (context as EditAdsActivity).editImagePos = adapterPosition
            }

            viewBinding.imDeleteImage.setOnClickListener{

                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.adapterCallback.onItemDelete()
            }

            viewBinding.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            //ImageManager.chooseScaleType(viewBinding.imageContent, bitmap)
            viewBinding.imageContent.setImageBitmap(bitmap)
        }
    }

    fun updateAdapter(newList: List<Bitmap>, needClear: Boolean) {
        if (needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }
}