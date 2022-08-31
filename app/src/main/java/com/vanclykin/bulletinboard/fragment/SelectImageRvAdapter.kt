package com.vanclykin.bulletinboard.fragment

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanclykin.bulletinboard.R
import com.vanclykin.bulletinboard.act.EditAdsActivity
import com.vanclykin.bulletinboard.utils.ImagePicker
import com.vanclykin.bulletinboard.utils.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_frag_item, parent, false)
        return ImageHolder(view, parent.context, this)
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

    class ImageHolder(itemView: View, var context: Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTitle: TextView
        lateinit var image: ImageView
        lateinit var imEditImage: ImageButton
        lateinit var imDeleteImage: ImageButton

        fun setData(item: String) {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageContent)
            imEditImage = itemView.findViewById(R.id.imEditImage)
            imDeleteImage = itemView.findViewById(R.id.imDeleteImage)

            imEditImage.setOnClickListener{
                ImagePicker.getImages(context as EditAdsActivity, 1, ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGES)
                (context as EditAdsActivity).editImagePos = adapterPosition
            }

            imDeleteImage.setOnClickListener{
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
            }

            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            image.setImageURI(Uri.parse(item))
        }
    }

    fun updateAdapter(newList: List<String>, needClear: Boolean) {
        if (needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }
}