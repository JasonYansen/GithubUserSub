package com.example.githubusersub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.githubusersub.data.model.ItemsItem
import com.example.githubusersub.databinding.ItemUserBinding

class UserAdapter(private val data:MutableList<ItemsItem> = mutableListOf(),
                  private val listener: (ItemsItem) -> Unit
):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(data: List<ItemsItem>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val v: ItemUserBinding) : RecyclerView.ViewHolder(v.root){
        fun bind(item: ItemsItem) {
            v.image.load(item.avatarUrl){
                transformations(RoundedCornersTransformation(12f))
            }

            v.username.text = item.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

}