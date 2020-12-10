package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuinden.liveevent.observe
import com.zhuinden.mvvmaacrxjavaretrofitroom.R
import com.zhuinden.mvvmaacrxjavaretrofitroom.application.CustomApplication
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import com.zhuinden.mvvmaacrxjavaretrofitroom.databinding.FragmentCatsBinding
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.bottomScrolledEvents
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.fragmentViewModels
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class CatFragment : Fragment(R.layout.fragment_cats), CatAdapter.OnItemClicked {
    private val catViewModel by fragmentViewModels {
        val application = requireContext().applicationContext as CustomApplication

        with(application) {
            CatViewModel(catDao, catDownloadManager, uiThreadScheduler)
        }
    }

    private lateinit var catAdapter: CatAdapter

    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCatsBinding.bind(view)

        with(binding) {
            with(catRecyclerView) {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                adapter = CatAdapter(this@CatFragment).also {
                    catAdapter = it
                }
            }

            compositeDisposable += catViewModel.cats.subscribeBy(onNext = { cats ->
                catAdapter.updateData(cats)
            })

            compositeDisposable += catRecyclerView.bottomScrolledEvents()
                .subscribeBy(onNext = { isScroll ->
                    catViewModel.handleScrollToBottom(isScroll)
                })

            catViewModel.openCatEvent.observe(viewLifecycleOwner) { cat ->
                val activity = requireActivity()
                activity.startActivity(Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(cat.sourceUrl);
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onItemClicked(cat: Cat) {
        catViewModel.openCatPage(cat)
    }
}
