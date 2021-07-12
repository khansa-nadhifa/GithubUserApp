package com.khansa.consumerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khansa.consumerapp.databinding.ItemRowUserBinding
import com.khansa.consumerapp.entity.DataUser

class UserAdapter(var data: MutableList<Any>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var onSetClickCallback: OnSetClickCallback? = null


    fun setData (item: ArrayList<DataUser>) {
        data.clear()
        data.addAll(item)
        notifyDataSetChanged()
    }

    fun setOnClickCallback (onSetClickCallback: OnSetClickCallback) {
        this.onSetClickCallback = onSetClickCallback
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemRowUserBinding.bind(view)

        fun bind (user: DataUser, position: Int) {
            with(binding) {
                tvItemName.text = user.username
                Glide.with(itemView.context)
                    .load(user.photo)
                    .into(imgItemPhoto)
                itemView.setOnClickListener {
                    onSetClickCallback?.onClicked(user, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position] as DataUser, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnSetClickCallback {
        fun onClicked(dataUser: DataUser, position: Int)
    }

    fun addItem(user: DataUser) {
        this.data.add(user)
        notifyItemInserted(this.data.size - 1)
    }

    fun removeItem(position: Int) {
        this.data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.data.size)
    }
}