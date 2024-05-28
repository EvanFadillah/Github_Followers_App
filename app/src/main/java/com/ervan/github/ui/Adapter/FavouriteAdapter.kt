package com.ervan.github.ui.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.ervan.github.data.database.FavouriteUser
import com.ervan.github.databinding.ItemUserBinding

class FavouriteAdapter(private val data: MutableList<FavouriteUser> = mutableListOf(),
                       private val listener:(FavouriteUser) -> Unit) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>(){

        fun setData(data: MutableList<FavouriteUser>) {
            this.data.clear()
            this.data.addAll(data)
            notifyDataSetChanged()
        }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavouriteUser) {
            binding.userImageView.load(item.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            binding.userTextView.text = item.login
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent,false))


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

}







