package com.example.camera_new_way

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.camera_new_way.databinding.ItemProductBinding


class RvAdapter(var list: ArrayList<Product>) : RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(product: Product) {
            val bind = ItemProductBinding.bind(itemView)
            bind.name.text = product.name
            bind.title.text = "${product.price} so'm"

            bind.img.setImageURI(Uri.parse(product.img))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}