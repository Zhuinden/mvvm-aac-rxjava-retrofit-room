package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zhuinden.mvvmaacrxjavaretrofitroom.R
import com.zhuinden.mvvmaacrxjavaretrofitroom.application.GlideApp
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import com.zhuinden.mvvmaacrxjavaretrofitroom.databinding.ViewCatItemBinding
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.inflate
import java.util.*

class CatAdapter(private val listener: OnItemClicked) : RecyclerView.Adapter<CatAdapter.ViewHolder>() {
    interface OnItemClicked {
        fun onItemClicked(cat: Cat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ViewCatItemBinding.bind(parent.inflate(R.layout.view_cat_item)))

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cats[position])
    }

    private var cats: List<Cat> = Collections.emptyList()

    fun updateData(cats: List<Cat>?) {
        this.cats = cats ?: Collections.emptyList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ViewCatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val onClickListener: View.OnClickListener = View.OnClickListener {
            val position = adapterPosition.takeIf { it >= 0 } ?: return@OnClickListener

            val cat = cats[position]

            listener.onItemClicked(cat)
        }

        init {
            binding.catItemContainer.setOnClickListener(onClickListener)
        }

        fun bind(cat: Cat) {
            GlideApp.with(binding.root)
                .load(cat.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.catImage)
        }
    }
}