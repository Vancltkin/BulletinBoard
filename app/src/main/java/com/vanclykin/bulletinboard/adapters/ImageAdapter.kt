package com.vanclykin.bulletinboard.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vanclykin.bulletinboard.R

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    val mainArray = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_adapter_item, parent,false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) { // из массива достаем эл и заполняем наж виев холдер
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {    //Размер данного списка сколько эл нужно нарисовать
        return mainArray.size
    }

    class ImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var  imItem: ImageView
        fun setData(uri: String){
            imItem = itemView.findViewById(R.id.imItem)
            imItem.setImageURI(Uri.parse(uri))
        }
    }

    fun update(newList: ArrayList<String>){

        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}