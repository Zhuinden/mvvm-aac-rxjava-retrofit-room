package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zhuinden.mvvmaacrxjavaretrofitroom.MainActivity
import com.zhuinden.mvvmaacrxjavaretrofitroom.R
import com.zhuinden.mvvmaacrxjavaretrofitroom.application.GlideApp
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.findActivity
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_cat_item.*
import org.jetbrains.anko.sdk15.listeners.onClick
import java.util.*

class CatAdapter : RecyclerView.Adapter<CatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.view_cat_item))

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cats[position])
    }

    private var cats: List<Cat> = Collections.emptyList()

    fun updateData(cats: List<Cat>?) {
        this.cats = cats ?: Collections.emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(cat: Cat) {
            GlideApp.with(containerView).load(cat.url).diskCacheStrategy(DiskCacheStrategy.ALL).into(catImage)
            catItemContainer.onClick { view ->
                val activity = view!!.findActivity<Activity>()
                activity.startActivity(Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(cat.sourceUrl);
                })
            }
        }
    }
}